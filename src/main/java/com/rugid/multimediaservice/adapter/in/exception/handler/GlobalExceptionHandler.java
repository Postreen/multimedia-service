package com.rugid.multimediaservice.adapter.in.exception.handler;

import com.rugid.multimediaservice.adapter.in.exception.DtoNotValidException;
import com.rugid.multimediaservice.adapter.in.exception.InvalidInputException;
import com.rugid.multimediaservice.domain.core.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            FileReadingException.class,
            FileWritingException.class,
            FileDeletingException.class,
            FolderInitializationException.class
    })
    public ResponseEntity<?> handleFileException(RuntimeException exception) {
        return createResponse(
                "File operation exception",
                exception,
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<?> handleInvalidInputException(InvalidInputException exception) {
        return createResponse(
                "Invalid input",
                exception,
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(NoSuchFileRuntimeException.class)
    public ResponseEntity<?> handleNoSuchFileException(NoSuchFileRuntimeException exception) {
        return createResponse(
                "File not found",
                exception,
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(WrongPathException.class)
    public ResponseEntity<?> handleWrongPathException(WrongPathException exception) {
        return createResponse(
                "Invalid file path",
                exception,
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(DtoNotValidException.class)
    public ResponseEntity<?> handleDtoException(DtoNotValidException exception) {
        Map<String, Object> jsonResult = new LinkedHashMap<>();
        jsonResult.put("Exception", "Json validation exception");
        jsonResult.put("Violations", exception.getErrorMessages());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(jsonResult);
    }

    private ResponseEntity<?> createResponse(String exceptionName, Exception exception, HttpStatus status) {
        Map<String, Object> jsonResult = new LinkedHashMap<>();
        jsonResult.put("Exception", exceptionName);
        jsonResult.put("Message", exception.getMessage());

        return ResponseEntity
                .status(status)
                .body(jsonResult);
    }
}