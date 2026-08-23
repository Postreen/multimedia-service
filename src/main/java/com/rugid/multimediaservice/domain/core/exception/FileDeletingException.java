package com.rugid.multimediaservice.domain.core.exception;

public class FileDeletingException extends RuntimeException {
    public FileDeletingException(Throwable cause) {
        super("Could not delete file", cause);
    }
}
