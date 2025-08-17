package com.project.exceptions;

import org.springframework.mail.MailException;

public class EmailSendingException extends Exception{

	public EmailSendingException(String msg, MailException e) {
		super(msg,e);
	}
}
