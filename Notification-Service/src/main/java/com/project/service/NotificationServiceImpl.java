package com.project.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.project.DTO.AgentDTO;
import com.project.DTO.ClaimDTO;
import com.project.DTO.CustomerDTO;
import com.project.DTO.PolicyDTO;
import com.project.client.AgentClient;
import com.project.client.ClaimClient;
import com.project.client.CustomerClient;
import com.project.client.PolicyClient;
import com.project.exceptions.AgentNotFoundException;
import com.project.exceptions.ClaimNotFoundException;
import com.project.exceptions.CustomerNotFoundException;
import com.project.exceptions.EmailSendingException;
import com.project.exceptions.PolicyNotFoundException;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@Service
public class NotificationServiceImpl implements NotificationService {

    // Initialize logger for this class
    private static final Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);

    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private CustomerClient customerclient;
    @Autowired
    private PolicyClient policyclient;
    @Autowired
    private AgentClient agentclient;
    @Autowired
    private ClaimClient claimclient;
    
    // SMS 
    @Value("${twilio.account.sid}")
    private String accountSid;
    @Value("${twilio.auth.token}")
    private String authToken;
    @Value("${twilio.trial.number}")
    private String trialNumber;

    @Value("${spring.mail.username}")
    String senderEmail;
    String appName = "Insurance Management System";
    String loginUrl = "InsruanceManagement.com";

    /**
     * Sends a registration confirmation email to a new customer.
     * @param customer The CustomerDTO containing customer details.
     * @throws EmailSendingException if there's an issue sending the email.
     */
    @Async
    public void sendRegisteredEmail(CustomerDTO customer) throws EmailSendingException {
        if (customer == null || customer.getEmail() == null) {
            // Using logger.error instead of System.err.println
            logger.error("Cannot send registered email: Customer object or email is null.");
            return;
        }

        try {
            String to = customer.getEmail();
            String customerName = customer.getName() != null ? customer.getName() : "Valued Customer";
            String subject = "Welcome to " + appName + "! Your Registration is Complete 🎉";
            String body = "Dear " + customerName + ",\n\n"
                    + "Welcome to " + appName + "! We are thrilled to have you as a new member.\n\n"
                    + "Your registration was successful, and your account is now active.\n\n"
                    + "You can now log in to your dashboard to manage your policies, view claims, and access exclusive features:\n"
                    + loginUrl + "\n\n"
                    + "If you have any questions or need assistance, please do not hesitate to contact our support team.\n\n"
                    + "Thank you for choosing " + appName + ".\n\n"
                    + "Sincerely,\n"
                    + "The " + appName + " Team";

            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(senderEmail);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            javaMailSender.send(message);
            sendWelcomeSms(customer.getPhone() , customer.getName());
            // Using logger.info instead of System.out.println
            logger.info("Registered Mail Sent Successfully to {}", to);
        } catch (MailException e) {
            // Using logger.error with the exception object for stack trace
            logger.error("Failed to send registered email to {}.", customer.getEmail(), e);
            throw new EmailSendingException("Failed to send registered email to " + customer.getEmail(), e);
        }
    }
    
    public void sendWelcomeSms(Long to ,String customerName) {
    		
    		String num = "+91"+to.toString();
    		System.out.println(num+"################################################################################################");
    		String body = "Thank you so much "+customerName+" for Chossing Us.";
    		Twilio.init(accountSid, authToken);
    		Message message = Message.creator(new PhoneNumber(num),new PhoneNumber(trialNumber),body).create();
    		logger.info("Welcome Sms Sent to the User", message.getSid(), to);    	
    }

    /**
     * Sends a policy opted confirmation email to a customer.
     * @param policy The PolicyDTO containing policy details.
     * @throws CustomerNotFoundException if the customer is not found.
     * @throws EmailSendingException if there's an issue sending the email.
     */
    @Override
    @Async
    public void sendMailPolicyOpted(PolicyDTO policy) throws EmailSendingException {
        CustomerDTO customer = customerclient.getCustomerById(policy.getCustomerId());
        if (customer == null) {
            logger.error("Customer with ID {} not found for policy {} opted email.", policy.getCustomerId(), policy.getPolicyId());
            throw new CustomerNotFoundException("Customer with ID " + policy.getCustomerId() + " not found.");
        }

        try {
            String to = customer.getEmail();
            String customerName = customer.getName();
            String policyName = policy.getName();
            String policyDetails = policy.getCoverageDetails();

            String subject = "Congratulations! Your " + policyName + " is Active with " + appName + "! 🎉";
            String body = "Dear " + customerName + ",\n\n"
                    + "We're excited to confirm that your application for the **" + policyName
                    + "** policy has been successfully processed. Your new policy is now active and ready to provide you with peace of mind!\n\n"
                    + "Here’s a summary of your new policy:\n"
                    + "  - **Policy Name:** " + policyName + "\n"
                    + "  - **Key Details:** " + (policyDetails != null && !policyDetails.isEmpty() ? policyDetails : "Please log in for full details.") + "\n\n"
                    + "You can access all the specifics, download your policy documents, and manage your coverage anytime by logging into your "
                    + appName + " account here:\n"
                    + loginUrl + "\n\n"
                    + "We are committed to providing you with excellent service and comprehensive coverage. If you have any questions or need further assistance, please don't hesitate to reach out to our dedicated support team.\n\n"
                    + "Thank you for choosing " + appName + ".\n\n"
                    + "Sincerely,\n"
                    + "The " + appName + " Team";

            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(senderEmail);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            javaMailSender.send(message);
            logger.info("Policy Opted Mail Sent Successfully to {} for policy {}", to, policy.getPolicyId());
        } catch (MailException e) {
            logger.error("Failed to send policy opted email to {} for policy {}.", customer.getEmail(), policy.getPolicyId(), e);
            throw new EmailSendingException("Failed to send policy opted email to " + customer.getEmail(), e);
        }
    }

    /**
     * Sends claim filing confirmation email to customer and claim assignment email to agent.
     * @param claim The ClaimDTO containing claim details.
     * @return A confirmation message.
     * @throws IllegalArgumentException if the claim object is null.
     * @throws PolicyNotFoundException if the policy associated with the claim is not found.
     * @throws AgentNotFoundException if the agent associated with the claim is not found.
     * @throws CustomerNotFoundException if the customer associated with the claim is not found.
     * @throws EmailSendingException if there's an issue sending the email.
     */
    @Override
    public String claimFilledEmail(ClaimDTO claim) throws PolicyNotFoundException, AgentNotFoundException, EmailSendingException {
        if (claim == null) {
            logger.error("Claim object cannot be null for claim filled email.");
            throw new IllegalArgumentException("Claim object cannot be null.");
        }

        // Fetching required data
        ResponseEntity<PolicyDTO> policyEntity = policyclient.getPolicyById(claim.getPolicyId());
        if (!policyEntity.hasBody() || policyEntity.getBody() == null) {
            logger.error("Policy with ID {} not found for claim filled email.", claim.getPolicyId());
            throw new PolicyNotFoundException("Policy with ID " + claim.getPolicyId() + " not found.");
        }
        PolicyDTO policy = policyEntity.getBody();

        ResponseEntity<AgentDTO> agentEntity = agentclient.getAgentById(claim.getAgentId());
        if (!agentEntity.hasBody() || agentEntity.getBody() == null) {
            logger.error("Agent with ID {} not found for claim filled email.", claim.getAgentId());
            throw new AgentNotFoundException("Agent with ID " + claim.getAgentId() + " not found.");
        }
        AgentDTO assignedAgent = agentEntity.getBody();

        CustomerDTO customer = customerclient.getCustomerById(claim.getCustomerId());
        if (customer == null) {
            logger.error("Customer with ID {} not found for claim filled email.", claim.getCustomerId());
            throw new CustomerNotFoundException("Customer with ID " + claim.getCustomerId() + " not found.");
        }

        try {
            // Send email to customer
            sendClaimConfirmationEmailToCustomer(claim, customer, policy, assignedAgent);
            // Send email to agent
            sendNewClaimAssignmentEmailToAgent(claim, customer, policy, assignedAgent);
            
            logger.info("Claim Filled Emails sent successfully for Claim ID {}", claim.getClaimId());
            return "Mail Sent to Customer and Agent Successfully";
        } catch (MailException e) {
            logger.error("Failed to send claim filed email for claim ID {}.", claim.getClaimId(), e);
            throw new EmailSendingException("Failed to send claim filed email for claim ID " + claim.getClaimId(), e);
        }
    }

    /**
     * Helper method to send claim confirmation email to the customer.
     */
    private void sendClaimConfirmationEmailToCustomer(ClaimDTO claim, CustomerDTO customer, PolicyDTO policy, AgentDTO assignedAgent) {
        String subject = "Confirmation: Your Claim #" + claim.getClaimId() + " Has Been Filed with " + appName + " 🎉";
        String body = "Dear " + customer.getName() + ",\n\n"
                + "We confirm that your claim (Claim ID: **" + claim.getClaimId() + "**) related to policy **" + policy.getPolicyId() + "** has been successfully filed with " + appName + ".\n\n"
                + "**Claim Details Submitted:**\n"
                + "  - Claim ID: " + claim.getClaimId() + "\n"
                + "  - Associated Policy: " + policy.getPolicyId() + "\n"
                + "  - Claimed Amount: ₹" + (claim.getClaimAmount() != null ? String.format("%,.2f", claim.getClaimAmount()) : "N/A") + "\n\n"
                + "Our team is now reviewing your claim. You can track the real-time status of your claim and find all related documents by logging into your account here:\n"
                + loginUrl + "\n\n";

        if (assignedAgent != null && assignedAgent.getName() != null) {
            body += "Your dedicated agent, " + assignedAgent.getName() + ", will be assisting you with this claim.\n";
            if (assignedAgent.getContactInfo() != null) {
                body += "You can reach them at: " + assignedAgent.getContactInfo() + "\n";
            }
        } else {
            body += "Our support team will be in touch with any updates.\n\n";
        }

        body += "If you have any questions, please contact our support team.\n\n"
                + "Thank you for choosing " + appName + ".\n\n"
                + "Sincerely,\n"
                + "The " + appName + " Team";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(senderEmail);
        message.setTo(customer.getEmail());
        message.setSubject(subject);
        message.setText(body);
        javaMailSender.send(message);
        logger.info("Claim Filled Mail Sent to Customer {} for Claim ID {}", customer.getEmail(), claim.getClaimId());
    }

    /**
     * Helper method to send new claim assignment email to the agent.
     */
    private void sendNewClaimAssignmentEmailToAgent(ClaimDTO claim, CustomerDTO customer, PolicyDTO policy, AgentDTO assignedAgent) {
        String subject = "New Claim Assigned: Claim #" + claim.getClaimId() + " for Policy " + policy.getPolicyId();
        String body = "Dear " + assignedAgent.getName() + ",\n\n"
                + "A new claim has been filed and assigned to you for review and processing.\n\n"
                + "**Claim Details:**\n"
                + "  - Claim ID: " + claim.getClaimId() + "\n"
                + "  - Associated Policy: " + policy.getName() + " (Policy No: " + policy.getPolicyId() + ")\n"
                + "  - Claimed Amount: ₹" + (claim.getClaimAmount() != null ? String.format("%,.2f", claim.getClaimAmount()) : "N/A") + "\n\n"
                + "**Customer Details:**\n"
                + "  - Customer Name: " + customer.getName() + "\n"
                + "  - Customer Email: " + customer.getEmail() + "\n\n"
                + "Please log in to your agent dashboard at " + loginUrl
                + " to review the full claim details and take necessary action.\n\n"
                + "Thank you,\n"
                + "The " + appName + " Team";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(senderEmail);
        message.setTo(assignedAgent.getContactInfo());
        message.setSubject(subject);
        message.setText(body);
        javaMailSender.send(message);
        logger.info("Claim Filed Mail Sent to Agent {} for Claim ID {}", assignedAgent.getContactInfo(), claim.getClaimId());
    }

    /**
     * Sends an email to the customer about their claim status update (Approved/Rejected).
     * @param claimId The ID of the claim.
     * @param status The updated status of the claim.
     * @return A confirmation message.
     * @throws ClaimNotFoundException if the claim is not found.
     * @throws CustomerNotFoundException if the customer is not found.
     * @throws PolicyNotFoundException if the policy is not found.
     * @throws EmailSendingException if there's an issue sending the email.
     */
    public String sendMailClaimUpdated(Integer claimId, ClaimDTO.Status status) throws PolicyNotFoundException, ClaimNotFoundException, EmailSendingException {
        ResponseEntity<ClaimDTO> claimEntity = claimclient.getClaimById(claimId);
        if (!claimEntity.hasBody() || claimEntity.getBody() == null) {
            logger.error("Claim with ID {} not found for claim update email.", claimId);
            throw new ClaimNotFoundException("Claim with ID " + claimId + " not found.");
        }
        ClaimDTO claim = claimEntity.getBody();

        CustomerDTO customer = customerclient.getCustomerById(claim.getCustomerId());
        if (customer == null) {
            logger.error("Customer with ID {} not found for claim update email for claim {}.", claim.getCustomerId(), claimId);
            throw new CustomerNotFoundException("Customer with ID " + claim.getCustomerId() + " not found.");
        }
        
        ResponseEntity<PolicyDTO> policyEntity = policyclient.getPolicyById(claim.getPolicyId());
        if (!policyEntity.hasBody() || policyEntity.getBody() == null) {
            logger.error("Policy with ID {} not found for claim update email for claim {}.", claim.getPolicyId(), claimId);
            throw new PolicyNotFoundException("Policy with ID " + claim.getPolicyId() + " not found.");
        }
        PolicyDTO policy = policyEntity.getBody();

        try {
            String subject = "", body = "";
            if (status == ClaimDTO.Status.APPROVED) {
                subject = "Great News! Your Claim #" + claimId + " Has Been Approved! 🎉";
                body = "Dear " + customer.getName() + ",\n\n"
                        + "We are delighted to inform you that your claim (Claim ID: **" + claimId + "**) "
                        + "related to policy **" + policy.getName() + " (No: " + policy.getPolicyId() + ")** has been **APPROVED**.\n\n"
                        + "**Claim Summary:**\n"
                        + "  - Claim ID: " + claimId + "\n"
                        + "  - Policy: " + policy.getName() + " (No: " + policy.getPolicyId() + ")\n"
                        + "  - Status: APPROVED\n"
                        + "\n"
                        + "The approved amount will be processed and disbursed according to your chosen method.\n\n"
                        + "You can view the full details and disbursement status by logging into your account here:\n"
                        + loginUrl + "\n\n"
                        + "If you have any questions, please contact our support team.\n\n"
                        + "Thank you for choosing " + appName + ".\n\n"
                        + "Sincerely,\n"
                        + "The " + appName + " Team";

            } else {
                subject = "Important Update: Your Claim #" + claimId + " Status - Rejected";
                body = "Dear " + customer.getName() + ",\n\n"
                        + "We regret to inform you that your claim (Claim ID: **" + claimId + "**) "
                        + "related to policy **" + policy.getName() + " (No: " + policy.getPolicyId() + ")** has been **REJECTED**.\n\n"
                        + "**Claim Summary:**\n"
                        + "  - Claim ID: " + claimId + "\n"
                        + "  - Policy: " + policy.getName() + " (No: " + policy.getPolicyId() + ")\n"
                        + "  - Status: REJECTED\n"
                        + "\n"
                        + "We understand this news may be disappointing. You can view the full details of the decision and appeal options by logging into your account here:\n"
                        + loginUrl + "\n\n"
                        + "If you have any further questions or wish to appeal, please contact our support team.\n\n"
                        + "Thank you for your understanding.\n\n"
                        + "Sincerely,\n"
                        + "The " + appName + " Team";
            }

            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(senderEmail);
            message.setTo(customer.getEmail());
            message.setSubject(subject);
            message.setText(body);
            javaMailSender.send(message);
            sendSmsForClaimUpdateToCustomer(customer.getPhone(),subject);
            logger.info("Claim Update Mail Sent to Customer {} for Claim ID {} with status {}", customer.getEmail(), claimId, status);

            return "Mail Sent to Customer For Claim Updation";
        } catch (MailException e) {
            logger.error("Failed to send claim updated email for claim ID {} with status {}.", claimId, status, e);
            throw new EmailSendingException("Failed to send claim updated email for claim ID " + claimId, e);
        }
    }

    private void sendSmsForClaimUpdateToCustomer(Long phone, String subject) {

    	Twilio.init(accountSid, authToken);
    	Message message = Message.creator(new PhoneNumber("91"+phone.toString()) , new PhoneNumber(trialNumber), subject).create();
    	logger.info("Sms sent to the Customer for updation of the claim as msg :"+message);
	}

	/**
     * Sends policy renewal reminders to customers whose policies are expiring soon.
     * This method runs asynchronously.
     */
    @Async
    @Override
    public void sendPolicyRenewalReminders() {
        logger.info("Initiating policy renewal reminders check.");
        try {
            ResponseEntity<List<PolicyDTO>> allPoliciesEntity = policyclient.getAllPolicies();
            if (!allPoliciesEntity.hasBody() || allPoliciesEntity.getBody() == null || allPoliciesEntity.getBody().isEmpty()) {
                logger.info("No policies found to send reminders for.");
                return;
            }
            List<PolicyDTO> allPolicies = allPoliciesEntity.getBody();
            logger.debug("Fetched {} total policies.", allPolicies.size());

            LocalDate today = LocalDate.now();
            LocalDate oneWeekFromNow = today.plusWeeks(1);

            List<PolicyDTO> expiringPolicies = allPolicies.stream()
                    .filter(policy -> policy.getExpiryDate() != null && !policy.getExpiryDate().isBefore(today)
                            && !policy.getExpiryDate().isAfter(oneWeekFromNow))
                    .collect(Collectors.toList());

            if (expiringPolicies.isEmpty()) {
                logger.info("No policies are expiring in the next week.");
                return;
            }
            logger.info("Found {} policies expiring in the next week.", expiringPolicies.size());

            for (PolicyDTO policy : expiringPolicies) {
                try {
                    CustomerDTO customer = customerclient.getCustomerById(policy.getCustomerId());
                    if (customer == null) {
                        logger.warn("Skipping reminder for policy {}: Customer with ID {} not found.", policy.getPolicyId(), policy.getCustomerId());
                        continue;
                    }

                    String customerName = customer.getName() != null ? customer.getName() : "Valued Customer";
                    String policyNumber = policy.getPolicyId() != null ? policy.getPolicyId().toString() : "N/A";
                    String policyName = policy.getName() != null ? policy.getName() : "Your Policy";
                    LocalDate expirationDate = policy.getExpiryDate();

                    String subject = "Action Required: Your " + policyName + " Policy (" + policyNumber + ") is Expiring Soon! ⏰";
                    String body = "Dear " + customerName + ",\n\n"
                            + "This is a friendly reminder that your **" + policyName + "** policy (Policy Number: **" + policyNumber + "**) is set to expire on **" + expirationDate + "**.\n\n"
                            + "To ensure continuous coverage and peace of mind, please renew your policy at your earliest convenience.\n\n"
                            + "You can easily renew your policy by logging into your " + appName + " account here:\n" + loginUrl + "\n\n"
                            + "If you have already renewed or if you have any questions, please disregard this email or contact our support team for assistance.\n\n"
                            + "Thank you for choosing " + appName + ".\n\n"
                            + "Sincerely,\n"
                            + "The " + appName + " Team";

                    SimpleMailMessage message = new SimpleMailMessage();
                    message.setFrom(senderEmail);
                    message.setTo(customer.getEmail());
                    message.setSubject(subject);
                    message.setText(body);
                    javaMailSender.send(message);
                    sendSmsRenewalReminder(subject ,"+91"+customer.getPhone());
                    logger.info("Renewal reminder sent for policy {} to customer {}.", policyNumber, customer.getEmail());
                } catch (MailException e) {
                    logger.error("Failed to send renewal reminder for policy {} to customer {}.", policy.getPolicyId(), customerclient.getCustomerById(policy.getCustomerId()).getEmail(), e);
                } catch (Exception e) {
                    logger.error("An unexpected error occurred while processing reminder for policy {}.", policy.getPolicyId(), e);
                }
            }
        } catch (Exception e) {
            logger.error("An error occurred while fetching policies for renewal reminders.", e);
            // Re-throw if you want this to propagate up as a general application error
            throw new RuntimeException("An error occurred while fetching policies for renewal reminders.", e);
        }
    }
 
    private void sendSmsRenewalReminder(String subject, String to) {
		Twilio.init(accountSid, authToken);
		Message message = Message.creator(new PhoneNumber(to), new PhoneNumber(trialNumber),subject).create();
		logger.info("Reminder Sms Sent : {} to {}" + message.getSid(), to);
		
	}

	/**
     * Sends an SMS message using Twilio.
     * @param to The recipient's phone number.
     * @param body The message body.
     * @return A string indicating success or failure.
     */
    public String sendActualSms(String to, String body) {
        try {
            Twilio.init(accountSid, authToken);
            Message message = Message.creator(new PhoneNumber(to), new PhoneNumber(trialNumber), body).create();
            logger.info("SMS sent successfully with SID: {} to {}.", message.getSid(), to);
            return "SMS sent successfully with SID: " + message.getSid();
        } catch (Exception e) {
            logger.error("Failed to send SMS to {}: {}", to, e.getMessage(), e);
            return "Failed to send SMS: " + e.getMessage();
        }
    }

    /**
     * Sends an email using JavaMailSender.
     * @param to The recipient's email address.
     * @param subject The email subject.
     * @param body The email body.
     * @return "True" if email sent successfully.
     * @throws EmailSendingException if there's an issue sending the email.
     */
    public String sendActualEmail(String to, String subject, String body) throws EmailSendingException {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(senderEmail);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            javaMailSender.send(message);
            logger.info("Mail Sent Successfully to {}.", to);
            return "True";
        } catch (MailException e) {
            logger.error("Failed to send email to {}.", to, e);
            throw new EmailSendingException("Failed to send email to " + to, e);
        }
    }
}