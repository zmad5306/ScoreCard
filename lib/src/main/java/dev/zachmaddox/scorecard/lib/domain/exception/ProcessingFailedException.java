package dev.zachmaddox.scorecard.lib.domain.exception;

import lombok.Getter;

import java.util.Map;
import java.util.Optional;

@Getter
public class ProcessingFailedException extends RuntimeException {
    private Map<String, String> metadata;
    public ProcessingFailedException(Map<String, String> metadata) {
        this.metadata = metadata;
    }
    public Optional<Map<String, String>> getMetadata() {
        return Optional.ofNullable(metadata);
    }
}

