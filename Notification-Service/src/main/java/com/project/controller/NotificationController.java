package com.project.controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.DTO.AgentCredentialsDTO;
import com.project.DTO.ClaimDTO;
import com.project.DTO.CustomerDTO;
import com.project.DTO.EmailDTO;
import com.project.DTO.PolicyDTO;
import com.project.DTO.SmsDTO;
import com.project.exceptions.AgentNotFoundException;
import com.project.exceptions.ClaimNotFoundException;
import com.project.exceptions.EmailSendingException;
import com.project.exceptions.PolicyNotFoundException;
import com.project.service.NotificationService;

/**
 * Controller for handling various notification-related end-points such as email and SMS alerts
 * for customer registration, policy updates, claim status, and reminders.
 * 
 * @author Shiv_Gupta
 */
@RestController
@RequestMapping("/notify")
public class NotificationController {
    private final NotificationService service;
    public NotificationController(NotificationService service) {
        this.service = service;
    }
    
    /**
     * Sends a welcome email and SMS to a newly registered customer.
     * 
     * @param customer the customer details
     * @return confirmation message after sending notifications
     * @throws EmailSendingException if email sending fails
     */
    @PostMapping("/customerRegistered")
    public String customerRegisteredMail(@RequestBody CustomerDTO customer) throws EmailSendingException {
        service.sendRegisteredEmail(customer);
        return "Welcome Mail/SMS Sent!!";
    }

    /**
     * Sends an email to the customer for the policies they have opted for.
     * 
     * @param policy the policy details
     * @return confirmation message after sending the email
     * @throws EmailSendingException if email sending fails
     */
    @PostMapping("/policieopted")
    public String mailPoliciesOpted(@RequestBody PolicyDTO policy) throws EmailSendingException {
        service.sendMailPolicyOpted(policy);
        return "Mail Sent For Opted Policy!!";
    }

    /**
     * Sends an email notification when a claim is filed.
     * 
     * @param claim the claim details
     * @return confirmation message after sending the email
     * @throws PolicyNotFoundException if the policy is not found
     * @throws AgentNotFoundException if the agent is not found
     * @throws EmailSendingException if email sending fails
     */
    @PostMapping("/claimfiled")
    public String claimFilled(@RequestBody ClaimDTO claim) throws PolicyNotFoundException, AgentNotFoundException, EmailSendingException {
        service.claimFilledEmail(claim);
        return "Mail Sent for Filing Claim!!";
    }

    /**
     * Sends an email notification when a claim status is updated.
     * 
     * @param claimId the ID of the claim
     * @param status the updated status of the claim
     * @return confirmation message after sending the email
     * @throws PolicyNotFoundException if the policy is not found
     * @throws ClaimNotFoundException if the claim is not found
     * @throws EmailSendingException if email sending fails
     */
    @PostMapping("/claimUpdated")
    public String claimUpdated(@RequestParam("claimId") Integer claimId, @RequestParam("status") ClaimDTO.Status status)
            throws PolicyNotFoundException, ClaimNotFoundException, EmailSendingException {
        service.sendMailClaimUpdated(claimId, status);
        return "Mail Sent for Updation Of the claim!!";
    }

    /**
     * Sends reminder emails to customers whose policies are expiring within 10 days.
     * 
     * @return confirmation message after sending reminders
     */
    @GetMapping("/reminder")
    public String sendPolicyRenewal() {
        service.sendPolicyRenewalReminders();
        return "Mail sent to Customers Whose Expiry is Within 10 days!!";
    }

    /**
     * Sends a custom SMS notification.
     * 
     * @param smsRequest the SMS details including recipient and message body
     * @return confirmation message after sending the SMS
     */
    @PostMapping("/actsms")
    public String sendSmsNotification(@RequestBody SmsDTO smsRequest) {
        return service.sendActualSms(smsRequest.getTo(), smsRequest.getBody());
    }
    
    /**
     * Sends a custom email notification.
     * 
     * @param emailRequest the email details including recipient, subject, and body
     * @return confirmation message after sending the email
     * @throws EmailSendingException if email sending fails
     */
    @PostMapping("/actemail")
    public String sendEmailNotification(@RequestBody EmailDTO emailRequest) throws EmailSendingException {
        return service.sendActualEmail(emailRequest.getTo(), emailRequest.getSubject(), emailRequest.getBody());
    }
    
    @PostMapping("/sendAgentCred")
    public String sendAgentCred(@RequestBody AgentCredentialsDTO cred) throws AgentNotFoundException, EmailSendingException {
    	service.sendAgentCred(cred);
    	return "credential Sent To the Agent :" + cred.getName();
    }
}
