package com.rugid.multimediaservice.domain.core.exception;

public class FolderInitializationException extends RuntimeException {
    public FolderInitializationException(Throwable cause) {
        super("Could not initialize folder");
    }
}
