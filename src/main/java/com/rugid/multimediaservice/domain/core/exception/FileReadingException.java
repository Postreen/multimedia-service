package com.rugid.multimediaservice.domain.core.exception;

public class FileReadingException extends RuntimeException {
    public FileReadingException(Throwable cause) {
        super("Could not read file", cause);
    }
}
