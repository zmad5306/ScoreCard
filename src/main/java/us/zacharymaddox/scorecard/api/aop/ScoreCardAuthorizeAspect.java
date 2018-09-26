package us.zacharymaddox.scorecard.api.aop;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import us.zacharymaddox.scorecard.api.domain.ScoreCardHeader;
import us.zacharymaddox.scorecard.api.domain.WaitException;
import us.zacharymaddox.scorecard.api.service.ScoreCardApiService;
import us.zacharymaddox.scorecard.domain.Authorization;

@Aspect
@Component
public class ScoreCardAuthorizeAspect {
	
	@Autowired
	private ScoreCardApiService scoreCardApiService;
	
	private Logger logger = LoggerFactory.getLogger(ScoreCardAuthorizeAspect.class);
	
	private ScoreCardHeader getScoreCardHeader(Method method, Object[] args) {
		Annotation[][] annotationMatrix = method.getParameterAnnotations();
		for (int i = 0; i < method.getParameterCount(); i++) {
			Annotation[] annotations = annotationMatrix[i];
			for (Annotation a : annotations) {
				if (a.annotationType() == Header.class) {
					Header annotation = (Header) a;
					if ("SCORE_CARD".equals(annotation.value())) {
						return scoreCardApiService.convertHeader((String) args[i]);
					}
				}
			}
		}
		return null;
	}
	
	@Around("@annotation(us.zacharymaddox.scorecard.api.annotation.ScoreCardAuthorize)")
	public Object enableScoreCard(ProceedingJoinPoint joinPoint) throws Throwable {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();
		ScoreCardHeader scoreCardHeader = getScoreCardHeader(method, joinPoint.getArgs());
		if (null != scoreCardHeader) {
			Authorization auth = scoreCardApiService.authorize(scoreCardHeader);
			switch (auth) {
				case PROCESS:
					return joinPoint.proceed();
				case WAIT:
					throw new WaitException();
				default:
					return null;
			}
		} else {
			logger.warn(String.format("The %s method of %s was annotated with @ScoreCardEnabled however it did not have a valid @Header(\"SCORE_CARD\") annotation on any of its arguments.", method.getName(), method.getDeclaringClass().getName()));
			return joinPoint.proceed();
		}
	}

}
