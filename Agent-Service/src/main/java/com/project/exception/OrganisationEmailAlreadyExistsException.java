package com.project.exception;

public class OrganisationEmailAlreadyExistsException extends RuntimeException {
    public OrganisationEmailAlreadyExistsException(String message) {
        super(message);
    }
}