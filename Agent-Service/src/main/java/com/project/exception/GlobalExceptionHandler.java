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
		return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body("Something went wrong but try again");
	}
	
	@ExceptionHandler(AgentAlreadyExistsException.class)
	public ResponseEntity<String> handleAgentAlreadyExists(AgentAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.SC_CONFLICT).body("Same agent registered again"); // 409 Conflict
    }
	@ExceptionHandler(OrganisationEmailAlreadyExistsException.class)
	public ResponseEntity<String> handleOrganisationEmailExists(OrganisationEmailAlreadyExistsException ex) {
	    return ResponseEntity.status(HttpStatus.SC_CONFLICT).body(ex.getMessage()); // 409 Conflict
	}
	@ExceptionHandler(ContactInfoAlreadyExistsException.class)
	public ResponseEntity<String> handleContactInfoExists(ContactInfoAlreadyExistsException ex) {
	    return ResponseEntity.status(HttpStatus.SC_CONFLICT).body(ex.getMessage()); // 409 Conflict
	}
	@ExceptionHandler(AdharcardNumberAlreadyExistException.class)
	public ResponseEntity<String> handleAdharcardAlreadyExist(AdharcardNumberAlreadyExistException e){
		return ResponseEntity.status(HttpStatus.SC_CONFLICT).body(e.getMessage()); // 409 Conflict
	}

	
	
	

}
