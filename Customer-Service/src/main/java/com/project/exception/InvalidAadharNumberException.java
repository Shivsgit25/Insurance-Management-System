package com.project.exception;

public class InvalidAadharNumberException extends RuntimeException {
    public InvalidAadharNumberException(String message) {
        super(message);
    }
}