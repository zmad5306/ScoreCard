package dev.zachmaddox.scorecard.lib.aspect;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.zachmaddox.scorecard.common.domain.Authorization;
import dev.zachmaddox.scorecard.common.domain.ScoreCardActionStatus;
import dev.zachmaddox.scorecard.lib.annotation.ProcessAuthorized;
import dev.zachmaddox.scorecard.lib.domain.ScoreCardHeader;
import dev.zachmaddox.scorecard.lib.domain.WaitException;
import dev.zachmaddox.scorecard.lib.domain.exception.ProcessingFailedException;
import dev.zachmaddox.scorecard.lib.service.ScoreCardApiService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Aspect
@Component
public class ProcessAuthorizedAspect {

    private static final String SCORE_CARD_HEADER = "SCORE_CARD";

    private final ScoreCardApiService scoreCardApiService;

    public ProcessAuthorizedAspect(@Qualifier("scoreCardApiServiceJms") ScoreCardApiService scoreCardApiService) {
        this.scoreCardApiService = scoreCardApiService;
    }

    private record HeaderValue(String rawHeader, ScoreCardHeader scoreCardHeader) {
    }

    private void updateStatus(HeaderValue headerValue, ScoreCardActionStatus status) {
        updateStatus(headerValue, status, Optional.empty());
    }

    private void updateStatus(HeaderValue headerValue, ScoreCardActionStatus status, Optional<Map<String, String>> metadata) {
        if (headerValue.scoreCardHeader() != null) {
            if (metadata.isPresent()) {
                scoreCardApiService.updateStatus(headerValue.scoreCardHeader(), status, metadata.get());
            }
            else {
                scoreCardApiService.updateStatus(headerValue.scoreCardHeader(), status);
            }

        } else {
            if (metadata.isPresent()) {
                scoreCardApiService.updateStatus(headerValue.rawHeader(), status, metadata.get());
            }
            else {
                scoreCardApiService.updateStatus(headerValue.rawHeader(), status);
            }

        }
    }

    private Optional<HeaderValue> toHeaderValue(Object header) {
        if (header instanceof String) {
            try {
                ScoreCardHeader scoreCardHeader = new ObjectMapper().readValue((String) header, ScoreCardHeader.class);
                return Optional.of(new HeaderValue((String) header, scoreCardHeader));
            } catch (JsonProcessingException e) {
                // skip
            }
        }

        return Optional.empty();
    }

    private Optional<HeaderValue> resolveHeader(ProceedingJoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();

        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];
            if (arg instanceof Message<?>) {
                try {
                    Object headers = arg.getClass().getMethod("getHeaders").invoke(arg);
                    Object scoreCardHeader = ((MessageHeaders) headers).get(SCORE_CARD_HEADER);
                    return toHeaderValue(scoreCardHeader);
                } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
                    // skip
                }
            }
        }

        return Optional.empty();
    }

    private static Optional<Map<String, String>> extractMetadata(Object result) {
        Map<String, String> metadata = new HashMap<>();

        if (result instanceof Optional<?> optional) {
            optional.ifPresent(value -> copyStrings(metadata, value));
        } else {
            copyStrings(metadata, result);
        }

        return metadata.isEmpty() ? Optional.empty() : Optional.of(metadata);
    }

    private static void copyStrings(Map<String, String> target, Object value) {
        if (value instanceof Map<?, ?> map) {
            map.forEach((key, val) -> {
                if (key instanceof String && val instanceof String) {
                    target.put((String) key, (String) val);
                }
            });
        }
    }

    @Around("@annotation(dev.zachmaddox.scorecard.lib.annotation.ProcessAuthorized)")
    public Object authorizeProcess(ProceedingJoinPoint joinPoint) throws Throwable {
        Optional<HeaderValue> headerValue = resolveHeader(joinPoint);
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        ProcessAuthorized annotation = method.getAnnotation(ProcessAuthorized.class);

        if (headerValue.isEmpty() && !annotation.allowMissingHeader()) {
            throw new IllegalArgumentException("Method requires valid Score Card header data.");
        }

        if (headerValue.isPresent()) {
            HeaderValue header = headerValue.get();
            Authorization authorization = header.scoreCardHeader() != null
                    ? scoreCardApiService.authorize(header.scoreCardHeader())
                    : scoreCardApiService.authorize(header.rawHeader());

            return switch (authorization) {
                case PROCESS -> {
                    try {
                        Object result = joinPoint.proceed(joinPoint.getArgs());
                        Optional<Map<String, String>> metadata = extractMetadata(result);
                        updateStatus(header, ScoreCardActionStatus.COMPLETED, metadata);
                        yield null;
                    } catch (ProcessingFailedException ex) {
                        updateStatus(header,  ScoreCardActionStatus.FAILED, ex.getMetadata());
                        yield null;
                    } catch (Exception e) {
                        // TODO log exception
                        // log.error(e.getMessage(), e);
                        Map<String, String> metadata = new HashMap<>();
                        metadata.put("errorMessage", e.getMessage());
                        updateStatus(header,  ScoreCardActionStatus.FAILED, Optional.of(metadata));
                        yield null;
                    }
                }
                case CANCEL -> {
                    updateStatus(header, ScoreCardActionStatus.CANCELLED);
                    yield null;
                }
                case SKIP -> null;
                case WAIT -> throw new WaitException();
            };
        }
        else {
            return joinPoint.proceed();
        }
    }

}
