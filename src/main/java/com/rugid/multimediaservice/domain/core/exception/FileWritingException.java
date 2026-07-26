package com.rugid.multimediaservice.domain.core.exception;

public class FileWritingException extends RuntimeException {
    public FileWritingException() {
        super("Could not save file");
    }
}
