package com.project.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler for REST APIs. This class intercepts exceptions
 * thrown by controller methods and returns appropriate HTTP responses.
 */
@RestControllerAdvice // Indicates that this class provides centralized exception handling across all
						// controllers
public class GlobalExceptionHandler {

	/**
	 * Handles ClaimNotFoundException specifically. This method is triggered when a
	 * ClaimNotFoundException is thrown in any controller.
	 *
	 * @param ex the exception instance containing the error details
	 * @return a ResponseEntity with the exception message and HTTP status 404 (Not
	 *         Found)
	 */
	@ExceptionHandler(ClaimNotFoundException.class)
	public ResponseEntity<String> handleClaimNotFound(ClaimNotFoundException ex) {
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
	}

	/**
	 * Handles all other uncaught exceptions. Acts as a fallback for any exception
	 * not explicitly handled by other methods.
	 *
	 * @param ex the exception instance containing the error details
	 * @return a ResponseEntity with a generic error message and HTTP status 500
	 *         (Internal Server Error)
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handleGeneralException(Exception ex) {
		return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<String> handleValidationErrors(MethodArgumentNotValidException ex) {
	    return ResponseEntity.badRequest().body("Validation failed: " + ex.getMessage());
	}

}
