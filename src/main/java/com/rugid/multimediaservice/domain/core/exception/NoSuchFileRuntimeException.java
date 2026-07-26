package com.rugid.multimediaservice.domain.core.exception;

public class NoSuchFileRuntimeException extends RuntimeException {

    public NoSuchFileRuntimeException(Throwable cause) {
        super("File not found", cause);
    }

}
