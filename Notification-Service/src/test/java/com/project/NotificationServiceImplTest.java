package com.project;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;

import com.project.exceptions.EmailSendingException;
import com.project.service.NotificationServiceImpl;

@ExtendWith(MockitoExtension.class)
@DisplayName("Notification Service SMS Tests")
class NotificationServiceImplTest {

	 @InjectMocks
	    private NotificationServiceImpl notificationService;

	    @Mock
	    private JavaMailSender javaMailSender;
	    // No need to mock other clients (CustomerClient, PolicyClient, etc.) for these specific methods.

	    @BeforeEach
	    void setUp() {
	        // Set the @Value fields using ReflectionTestUtils
	        ReflectionTestUtils.setField(notificationService, "senderEmail", "test@example.com");
	        ReflectionTestUtils.setField(notificationService, "accountSid", "test_sid");
	        ReflectionTestUtils.setField(notificationService, "authToken", "test_token");
	        ReflectionTestUtils.setField(notificationService, "trialNumber", "+15017122661");
	    }

	    // --- Test Cases for sendActualEmail ---

	    @Test
	    @DisplayName("sendActualEmail should send email successfully")
	    void sendActualEmail_Success() throws EmailSendingException {
	        String toEmail = "recipient@example.com";
	        String subject = "Test Subject";
	        String body = "Test Body";
	        doNothing().when(javaMailSender).send(any(SimpleMailMessage.class));
	        // Call the method under test
	        String result = notificationService.sendActualEmail(toEmail, subject, body);

	        // Verify that javaMailSender.send was called exactly once with any SimpleMailMessage
	        verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));

	        // Assert the return value
	        assertEquals("True", result);
	    }

	    @Test
	    @DisplayName("sendActualEmail should throw EmailSendingException on mail failure")
	    void sendActualEmail_Failure() {
	        String toEmail = "recipient@example.com";
	        String subject = "Test Subject";
	        String body = "Test Body";

	        // Configure javaMailSender to throw a MailException when send is called
	        doThrow(new MailException("Mock Mail Failure") {
				private static final long serialVersionUID = -5582883301773005167L;}).when(javaMailSender).send(any(SimpleMailMessage.class));

	        // Assert that EmailSendingException is thrown
	        EmailSendingException thrown = assertThrows(EmailSendingException.class, () ->
	            notificationService.sendActualEmail(toEmail, subject, body)
	        );

	        // Verify that javaMailSender.send was still called once
	        verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));

	        // Assert the message of the thrown exception
	        assertEquals("Failed to send email to " + toEmail, thrown.getMessage());
	    }
}