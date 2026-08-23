package com.rugid.multimediaservice.domain.core.exception;

public class WrongPathException extends RuntimeException {
    public WrongPathException() {
        super("Invalid file path");
    }
}
