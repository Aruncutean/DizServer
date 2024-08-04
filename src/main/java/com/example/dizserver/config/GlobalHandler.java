package com.example.dizserver.config;
import com.mongodb.MongoException;
import com.mongodb.MongoWriteException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@ControllerAdvice
public class GlobalHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, String>> handleConstraintViolationException(ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach((violation) -> {
            String[] parts = violation.getPropertyPath().toString().split("\\.");
            String fieldName = parts[parts.length - 1];
            String errorMessage = violation.getMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MongoWriteException.class)
    public ResponseEntity<Map<String, String>> handleMongoWriteException(MongoWriteException ex) {
        Map<String, String> errors = new HashMap<>();

        if (ex.getCode() == 11000) {
            String message = ex.getMessage();
            String duplicateKeyError = "Duplicate key : ";

            if (message != null) {
                int startIndex = message.indexOf("dup key: {");
                if (startIndex != -1) {
                    int endIndex = message.indexOf('}', startIndex);
                    if (endIndex != -1) {
                        duplicateKeyError += message.substring(startIndex + 10, endIndex); // Extract key part
                    }
                }
            }

            errors.put("error", duplicateKeyError);
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }

        errors.put("error", "Unexpected MongoDB write error.");
        return new ResponseEntity<>(errors, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MongoException.class)
    public ResponseEntity<Map<String, String>> handleMongoException(MongoException ex) {
        Map<String, String> errors = new HashMap<>();

        if (ex.getCode() == 11000) {

            String message = ex.getMessage();
            String duplicateKeyError = "Duplicate key : ";
            if (message != null) {
                int startIndex = message.indexOf("dup key: {");
                if (startIndex != -1) {
                    int endIndex = message.indexOf('}', startIndex);
                    if (endIndex != -1) {
                        duplicateKeyError += message.substring(startIndex + 10, endIndex);
                    }
                }
            }

            errors.put("error", duplicateKeyError);
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }

        errors.put("error", "Unexpected MongoDB write error.");
        return new ResponseEntity<>(errors, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Map<String, String>> handleNoSuchElementException(NoSuchElementException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("error", ex.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);
    }

}