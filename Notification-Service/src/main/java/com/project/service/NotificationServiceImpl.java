package com.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.project.DTO.CustomerDTO;
import com.project.DTO.PolicyDTO;
import com.project.client.CustomerClient;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@Service
public  class NotificationServiceImpl implements NotificationService{

	
	
//Policies Selected mail
	
	
	
//	Registered EMail Sent
	
	@Autowired
	private JavaMailSender javaMailSender;
	@Value("${spring.mail.username}")
	String senderEmail;
	String appName = "Insurance Management System";
	String loginUrl = "InsruanceManagement.com";
	@Async // This must be void for proper async behavior
	public void sendRegisteredEmail(CustomerDTO customer) {
		String to = customer.getEmail();
		String customerName = customer.getName() != null ? customer.getName() : "Valued Customer";

		String subject = "Welcome to " + appName + "! Your Registration is Complete 🎉";

		String body = "Dear " + customerName + ",\n\n" +
					  "Welcome to " + appName + "! We are thrilled to have you as a new member.\n\n" +
					  "Your registration was successful, and your account is now active.\n\n" +
					  "You can now log in to your dashboard to manage your policies, view claims, and access exclusive features:\n" +
					  loginUrl + "\n\n" +
					  "If you have any questions or need assistance, please do not hesitate to contact our support team.\n\n" +
					  "Thank you for choosing " + appName + ".\n\n" +
					  "Sincerely,\n" +
					  "The " + appName + " Team";

		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(senderEmail); // Correctly uses the injected senderEmail
		message.setTo(to);
		message.setSubject(subject);
		message.setText(body);
		javaMailSender.send(message);
		System.out.println("Mail Sent Successfully!!!");
	}
	

    
    //Policy Selection mail
    @Autowired
    CustomerClient customerclient;
    @Override
    @Async
    public void sendMailPolicyOpted(PolicyDTO policy) {
    	System.out.println(policy.getCustomerId()+"##########################################################################");
    	CustomerDTO customer = customerclient.getCustomerById(policy.getCustomerId());
    	System.out.println(customer.getName()+"############################################################");
    	
    	String to = customer.getEmail();
		String customerName = customer.getName();
		String policyName = policy.getName();
		String policyDetails = policy.getCoverageDetails(); // Can be a summary or a link

		String subject = "Congratulations! Your " + policyName + " is Active with " + appName + "! 🎉";
		
		String body = "Dear " + customerName + ",\n\n" +
					  "We're excited to confirm that your application for the **" + policyName + "** policy has been successfully processed. Your new policy is now active and ready to provide you with peace of mind!\n\n" +
					  "Here’s a summary of your new policy:\n" +
					  "  - **Policy Name:** " + policyName + "\n" +
					  "  - **Key Details:** " + (policyDetails != null && !policyDetails.isEmpty() ? policyDetails : "Please log in for full details.") + "\n\n" +
					  "You can access all the specifics, download your policy documents, and manage your coverage anytime by logging into your " + appName + " account here:\n" +
					  loginUrl + "\n\n" +
					  "We are committed to providing you with excellent service and comprehensive coverage. If you have any questions or need further assistance, please don't hesitate to reach out to our dedicated support team.\n\n" +
					  "Thank you for choosing " + appName + ".\n\n" +
					  "Sincerely,\n" +
					  "The " + appName + " Team";

    	
    	SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("your_email@gmail.com"); // Sender email, can also be retrieved from properties
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        javaMailSender.send(message);
        System.out.println("MAIl SENT#################");
        System.out.println("Policies Opted Successfully!!");
    }
	
//	SMS
	
	@Value("${twilio.account.sid}")
    private String accountSid;

    @Value("${twilio.auth.token}")
    private String authToken;

    @Value("${twilio.trial.number}")
    private String trialNumber;

//    @Async
    public String sendActualSms(String to, String body) {
        Twilio.init(accountSid, authToken);

        Message message = Message.creator(
                new PhoneNumber(to),
                new PhoneNumber(trialNumber),
                body)
            .create();

        return "SMS sent successfully with SID: " + message.getSid();
    }
    
    
	
//	@Async
    public String sendActualEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("your_email@gmail.com"); // Sender email, can also be retrieved from properties
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        javaMailSender.send(message);
        System.out.println("MAIl SENT#################");
        return "True";
       
    }



	

}
