package com.rugid.multimediaservice.domain.core.exception;

public class FileWritingException extends RuntimeException {
    public FileWritingException(Throwable cause) {
        super("Could not save file", cause);
    }
}
