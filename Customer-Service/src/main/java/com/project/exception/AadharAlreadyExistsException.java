package com.project.exception;

public class AadharAlreadyExistsException extends RuntimeException {
    public AadharAlreadyExistsException(String message) {
        super(message);
    }
}