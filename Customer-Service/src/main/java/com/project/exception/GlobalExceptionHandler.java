package com.project.exception;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(CustomerNotFoundException.class)
	public ResponseEntity<String> handleCustomerNotFoundException(CustomerNotFoundException ex) {
		logger.error("Customer not found: {}", ex.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	}
    
	 @ExceptionHandler(CustomerAlreadyExistsException.class)
	    public ResponseEntity<String> handleCustomerAlreadyExistsException(CustomerAlreadyExistsException ex) {
	        logger.error("Customer already exists: {}", ex.getMessage());
	        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
	    }

	@ExceptionHandler(ExternalServiceException.class)
	public ResponseEntity<String> handleExternalServiceException(ExternalServiceException ex) {
		logger.error("External service error: {}", ex.getMessage(), ex.getCause());
		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
				.body("An external service is currently unavailable. Please try again later.");
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handleGenericException(Exception ex) {
		logger.error("An unexpected error occurred: {}", ex.getMessage(), ex);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body("An unexpected error occurred. Please contact support.");
	}


//    @ExceptionHandler(AadharAlreadyExistsException.class)
//    public ResponseEntity<String> handleAadharAlreadyExistsException(AadharAlreadyExistsException ex) {
//       Map<String, String> response = new HashMap<>();
//        logger.error("Aadhaar already exists: {}", ex.getMessage());
//        return  ResponseEntity.status(HttpStatus.CONFLICT).body("already exist adhacard");
//    }
    @ExceptionHandler(AadharAlreadyExistsException.class)
    public ResponseEntity<String> handleAadharAlreadyExistsException(AadharAlreadyExistsException ex) {
        logger.error("Aadhar already exists: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Customer with Aadhar number already exists");
    }
    @ExceptionHandler(InvalidAadharNumberException.class)
    public ResponseEntity<Map<String, String>> handleInvalidAadharNumberException(InvalidAadharNumberException ex) {
        Map<String, String> response = new HashMap<>();
        logger.error("Invalid Aadhaar number: {}", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

  
}
