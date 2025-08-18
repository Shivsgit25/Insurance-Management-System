package com.project.exception;

import org.apache.hc.core5.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<String> handleResourcenotFound(ResourceNotFoundException ex){
		return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).body(ex.getMessage());
	}
	
	@ExceptionHandler(FeignClientException.class)
	public ResponseEntity<String> handleFeignError(FeignClientException ex){
		return ResponseEntity.status(HttpStatus.SC_BAD_GATEWAY).body("External service error"+ ex.getMessage());
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handleGenericError(Exception ex){
		return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body("Something went wrong");
	}
	
	
	
	

}
