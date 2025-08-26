package com.project.exception;

public class ClaimNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ClaimNotFoundException(String message) {
		super(message); // Pass the message to the superclass constructor
	}

	public ClaimNotFoundException() {
		super(); // Calls the default constructor of the superclass
	}
}
