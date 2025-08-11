package com.project.exception;

/**
 * Custom exception thrown when a claim is not found in the system. Typically
 * used in service or controller layers to indicate missing data.
 */
public class ClaimNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a new ClaimNotFoundException with the specified detail message.
	 *
	 * @param message the detail message explaining the reason for the exception
	 */
	public ClaimNotFoundException(String message) {
		super(message); // Pass the message to the superclass constructor
	}

	/**
	 * Constructs a new ClaimNotFoundException with no detail message. This
	 * constructor can be used when a generic exception is sufficient, or when the
	 * message will be set later or handled differently.
	 */
	public ClaimNotFoundException() {
		super(); // Calls the default constructor of the superclass
	}
}
