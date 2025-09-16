package com.project.exception;

public class ContactInfoAlreadyExistsException extends RuntimeException {
    public ContactInfoAlreadyExistsException(String message) {
        super(message);
    }
}