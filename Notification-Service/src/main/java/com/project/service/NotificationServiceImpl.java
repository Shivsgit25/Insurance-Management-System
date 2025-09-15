package com.project.service;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.project.DTO.AgentCredentialsDTO;
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
import com.project.model.InAppNotification;
import com.project.repository.InAppNotificationRepository;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@Service
public class NotificationServiceImpl implements NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);

    private static final String APP_NAME = "Insurance Management System";
    private static final String LOGIN_URL = "InsruanceManagement.com";
    private static final String DEFAULT_CUSTOMER_NAME = "Valued Customer";
    private static final String CONTACT_SUPPORT_TEXT = "If you have any questions or need assistance, please do not hesitate to contact our support team.";
    private static final String SINCERELY_TEXT = "Sincerely,\n" + "The " + APP_NAME + " Team";

    private final JavaMailSender javaMailSender;
    private final CustomerClient customerclient;
    private final PolicyClient policyclient;
    private final AgentClient agentclient;
    private final ClaimClient claimclient;
    private InAppNotificationRepository inappNotificationrepo;
    
    @Value("${twilio.account.sid}")
    private String accountSid;
    @Value("${twilio.auth.token}")
    private String authToken;
    @Value("${twilio.trial.number}")
    private String trialNumber;

    @Value("${spring.mail.username}")
    private String senderEmail;

    public NotificationServiceImpl(JavaMailSender javaMailSender, CustomerClient customerclient, PolicyClient policyclient, AgentClient agentclient, ClaimClient claimclient, InAppNotificationRepository inappNotificationrepo) {
        this.javaMailSender = javaMailSender;
        this.customerclient = customerclient;
        this.policyclient = policyclient;
        this.agentclient = agentclient;
        this.claimclient = claimclient;
        this.inappNotificationrepo = inappNotificationrepo;
    }

    /**
     * Helper method to create and send a SimpleMailMessage So that we dont need to rewrite again.
     */
    private void sendEmail(String to, String subject, String body) throws MailException {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(senderEmail);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        javaMailSender.send(message);
    }
    
    /**
     * Sends a registration confirmation email to a new customer.
     */
    @Async
    @Override
    public void sendRegisteredEmail(CustomerDTO customer) throws EmailSendingException {
        if (customer == null || customer.getEmail() == null) {
            logger.error("Cannot send registered email: Customer object or email is null.");
            return;
        }
        
        String customerName = Objects.toString(customer.getName(), DEFAULT_CUSTOMER_NAME);
        String subject = "Welcome to " + APP_NAME + "! Your Registration is Complete 🎉";
        String body = String.format(
            "Dear %s,\n\n"
            + "Welcome to %s! We are thrilled to have you as a new member.\n\n"
            + "Your registration was successful, and your account is now active.\n\n"
            + "You can now log in to your dashboard to manage your policies, view claims, and access exclusive features:\n"
            + "%s\n\n"
            + "%s\n\n"
            + "Thank you for choosing %s.\n\n"
            + SINCERELY_TEXT,
            customerName, APP_NAME, LOGIN_URL, CONTACT_SUPPORT_TEXT, APP_NAME
        );

        try {
            sendEmail(customer.getEmail(), subject, body);
            if (customer.getPhone() != null && customer.getPhone() != 0) {
                 sendWelcomeSms(customer.getPhone(), customer.getName());
            }
            logger.info("Registered Mail Sent Successfully to {}", customer.getEmail());
        } catch (MailException e) {
            logger.error("Failed to send registered email to {}.", customer.getEmail(), e);
            throw new EmailSendingException("Failed to send registered email to " + customer.getEmail(), e);
        }
    }

    public void sendWelcomeSms(Long to, String customerName) {
        try {
            String num = "+91" + to.toString();
            String body = String.format("Thank you so much %s for Choosing Us.", customerName);
            Twilio.init(accountSid, authToken);
            Message message = Message.creator(new PhoneNumber(num), new PhoneNumber(trialNumber), body).create();
            logger.info("Welcome SMS Sent with SID: {} to user: {}", message.getSid(), to);
        } catch(Exception e) {
            logger.error("Failed to send welcome SMS to user {}: {}", to, e.getMessage());
        }
    }

 
    /**
     * Sends a policy opted confirmation email to a customer.
     * @param policy The PolicyDTO containing policy details.
     * @throws CustomerNotFoundException if the customer is not found.
     * @throws EmailSendingException if there's an issue sending the email.
     */
    @Async
    @Override
    public void sendMailPolicyOpted(PolicyDTO policy) throws EmailSendingException {
        CustomerDTO customer = getCustomerOrThrow(policy.getCustomerId());

        String customerName = Objects.toString(customer.getName(), DEFAULT_CUSTOMER_NAME);
        String policyDetails = Objects.toString(policy.getCoverageDetails(), "Please log in for full details.");
        String subject = String.format("Congratulations! Your %s is Active with %s!", policy.getName(), APP_NAME);
        String body = String.format(
            "Dear %s,\n\n"
            + "We're excited to confirm that your application for the **%s** policy has been successfully processed. "
            + "Your new policy is now active and ready to provide you with peace of mind!\n\n"
            + "Here’s a summary of your new policy:\n"
            + "  - **Policy Name:** %s\n"
            + "  - **Key Details:** %s\n\n"
            + "You can access all the specifics, download your policy documents, and manage your coverage anytime by logging into your "
            + "%s account here:\n"
            + "%s\n\n"
            + "We are committed to providing you with excellent service and comprehensive coverage. %s\n\n"
            + "Thank you for choosing %s.\n\n"
            + SINCERELY_TEXT,
            customerName, policy.getName(), policy.getName(), policyDetails, APP_NAME, LOGIN_URL, APP_NAME ,APP_NAME
        );

        try {
            sendEmail(customer.getEmail(), subject, body);
            logger.info("Policy Opted Mail Sent Successfully to {} for policy {}", customer.getEmail(), policy.getPolicyId());

            // Add In-App Notification
            InAppNotification inAppNotification = new InAppNotification();
            inAppNotification.setSubject(subject);
            inAppNotification.setType("Policy Opted");
            inAppNotification.setDetails(body);
            inAppNotification.setCustomerId(customer.getCustomerId());
            addNotification(inAppNotification);
        } catch (MailException e) {
            logger.error("Failed to send policy opted email to {} for policy {}.", customer.getEmail(), policy.getPolicyId(), e);
            throw new EmailSendingException("Failed to send policy opted email to " + customer.getEmail(), e);
        }
    }


    /**
     * Sends claim filing confirmation email to customer and claim assignment email to agent.
     * @param claim The ClaimDTO containing claim details.
     * @throws IllegalArgumentException if the claim object is null.
     * @throws PolicyNotFoundException if the policy associated with the claim is not found.
     * @throws AgentNotFoundException if the agent associated with the claim is not found.
     * @throws CustomerNotFoundException if the customer associated with the claim is not found.
     * @throws EmailSendingException if there's an issue sending the email.
     */
    @Async
    @Override
    public void claimFilledEmail(ClaimDTO claim) throws PolicyNotFoundException, AgentNotFoundException, EmailSendingException {
        if (claim == null) {
            throw new IllegalArgumentException("Claim object cannot be null.");
        }
        
        PolicyDTO policy = getPolicyOrThrow(claim.getPolicyId());
        AgentDTO assignedAgent = getAgentOrThrow(claim.getAgentId());
        CustomerDTO customer = getCustomerOrThrow(claim.getCustomerId());

        try {
            sendClaimConfirmationEmailToCustomer(claim, customer, policy, assignedAgent);
            sendNewClaimAssignmentEmailToAgent(claim, customer, policy, assignedAgent);
            logger.info("Claim Filled Emails sent successfully for Claim ID {}", claim.getClaimId());
        } catch (MailException e) {
            logger.error("Failed to send claim filed email for claim ID {}.", claim.getClaimId(), e);
            throw new EmailSendingException("Failed to send claim filed email for claim ID " + claim.getClaimId(), e);
        }
    }
    
   

    /**
     * Helper method to send claim confirmation email to the customer.
     */
    private void sendClaimConfirmationEmailToCustomer(ClaimDTO claim, CustomerDTO customer, PolicyDTO policy, AgentDTO assignedAgent) {
        String subject = String.format("Confirmation: Your Claim #%d Has Been Filed with %s 🎉", claim.getClaimId(), APP_NAME);
        
        String agentInfo = (assignedAgent != null && assignedAgent.getName() != null && assignedAgent.getContactInfo() != null)
            ? String.format("Your dedicated agent, %s, will be assisting you with this claim.\nYou can reach them at: %s\n", assignedAgent.getName(), assignedAgent.getContactInfo())
            : "Our support team will be in touch with any updates.\n\n";

        String claimedAmount = (claim.getClaimAmount() != null) ? String.format("%,.2f", claim.getClaimAmount()) : "N/A";

        String body = String.format(
            "Dear %s,\n\n"
            + "We confirm that your claim (Claim ID: **%d**) related to policy **%s** has been successfully filed with %s.\n\n"
            + "**Claim Details Submitted:**\n"
            + "  - Claim ID: %d\n"
            + "  - Associated Policy: %s\n"
            + "  - Claimed Amount: ₹%s\n\n"
            + "Our team is now reviewing your claim. You can track the real-time status of your claim and find all related documents by logging into your account here:\n"
            + "%s\n\n"
            + "%s"
            + "If you have any questions, please contact our support team.\n\n"
            + "Thank you for choosing %s.\n\n"
            + SINCERELY_TEXT,
            Objects.toString(customer.getName(), DEFAULT_CUSTOMER_NAME),
            claim.getClaimId(),
            Objects.toString(policy.getPolicyId(), "N/A"),
            APP_NAME,
            claim.getClaimId(),
            Objects.toString(policy.getPolicyId(), "N/A"),
            claimedAmount,
            LOGIN_URL,
            agentInfo,
            APP_NAME
        );
        sendEmail(customer.getEmail(), subject, body);
        logger.info("Claim Filed Mail Sent to Customer {} for Claim ID {}", customer.getEmail(), claim.getClaimId());
        
        // Add In-App Notification
        InAppNotification inAppNotification = new InAppNotification();
        inAppNotification.setSubject(subject);
        inAppNotification.setType("Claim Filed");
        inAppNotification.setDetails(body);
        inAppNotification.setCustomerId(customer.getCustomerId());
        addNotification(inAppNotification);
    }

    /**
     * Helper method to send new claim assignment email to the agent.
     */
    
    private void sendNewClaimAssignmentEmailToAgent(ClaimDTO claim, CustomerDTO customer, PolicyDTO policy, AgentDTO assignedAgent) {
        String subject = String.format("New Claim Assigned: Claim #%d for Policy %s", claim.getClaimId(), policy.getPolicyId());
        
        String claimedAmount = (claim.getClaimAmount() != null) ? String.format("%,.2f", claim.getClaimAmount()) : "N/A";

        String body = String.format(
            "Dear %s,\n\n"
            + "A new claim has been filed and assigned to you for review and processing.\n\n"
            + "**Claim Details:**\n"
            + "  - Claim ID: %d\n"
            + "  - Associated Policy: %s (Policy No: %s)\n"
            + "  - Claimed Amount: ₹%s\n\n"
            + "**Customer Details:**\n"
            + "  - Customer Name: %s\n"
            + "  - Customer Email: %s\n\n"
            + "Please log in to your agent dashboard at %s to review the full claim details and take necessary action.\n\n"
            + "Thank you,\n"
            + "The %s Team",
            Objects.toString(assignedAgent.getName(), "Agent"),
            claim.getClaimId(),
            Objects.toString(policy.getName(), "N/A"),
            Objects.toString(policy.getPolicyId(), "N/A"),
            claimedAmount,
            Objects.toString(customer.getName(), DEFAULT_CUSTOMER_NAME),
            Objects.toString(customer.getEmail(), "N/A"),
            LOGIN_URL,
            APP_NAME
        );
        sendEmail(assignedAgent.getContactInfo(), subject, body);
        logger.info("Claim Filed Mail Sent to Agent {} for Claim ID {}", assignedAgent.getContactInfo(), claim.getClaimId());
        
        // Add In-App Notification
        InAppNotification inAppNotification = new InAppNotification();
        inAppNotification.setSubject(subject);
        inAppNotification.setType("Claim Assigned");
        inAppNotification.setDetails(body);
        inAppNotification.setAgentId(assignedAgent.getAgentId());
        addNotification(inAppNotification);
    }

    //-----------------------------------------------------------------------------------

    /**
     * Sends an email to the customer about their claim status update (Approved/Rejected).
     * @param claimId The ID of the claim.
     * @param status The updated status of the claim.
     * @throws ClaimNotFoundException if the claim is not found.
     * @throws CustomerNotFoundException if the customer is not found.
     * @throws PolicyNotFoundException if the policy is not found.
     * @throws EmailSendingException if there's an issue sending the email.
     */
    @Async
    public void sendMailClaimUpdated(Integer claimId, ClaimDTO.Status status) throws PolicyNotFoundException, ClaimNotFoundException, EmailSendingException {
        ClaimDTO claim = getClaimOrThrow(claimId);
        CustomerDTO customer = getCustomerOrThrow(claim.getCustomerId());
        PolicyDTO policy = getPolicyOrThrow(claim.getPolicyId());

        String subject;
        String body;
        
        if (status == ClaimDTO.Status.APPROVED) {
            subject = String.format("Great News! Your Claim #%d Has Been Approved! 🎉", claimId);
            body = createApprovedEmailBody(customer, claimId, policy);
        } else {
            subject = String.format("Important Update: Your Claim #%d Status - Rejected", claimId);
            body = createRejectedEmailBody(customer, claimId, policy);
        }

        try {
            sendEmail(customer.getEmail(), subject, body);
            logger.info("Claim Update Mail Sent to Customer {} for Claim ID {} with status {}", customer.getEmail(), claimId, status);

            // Add In-App Notification
            InAppNotification inAppNotification = new InAppNotification();
            inAppNotification.setSubject(subject);
            inAppNotification.setType("Claim Update");
            inAppNotification.setDetails(body);
            inAppNotification.setCustomerId(customer.getCustomerId());
            addNotification(inAppNotification);
        } catch (MailException e) {
            logger.error("Failed to send claim updated email for claim ID {} with status {}.", claimId, status, e);
            throw new EmailSendingException("Failed to send claim updated email for claim ID " + claimId, e);
        }
    }
    
    private String createApprovedEmailBody(CustomerDTO customer, Integer claimId, PolicyDTO policy) {
        return String.format(
            "Dear %s,\n\n"
            + "We are delighted to inform you that your claim (Claim ID: **%d**) "
            + "related to policy **%s (No: %s)** has been **APPROVED**.\n\n"
            + "**Claim Summary:**\n"
            + "  - Claim ID: %d\n"
            + "  - Policy: %s (No: %s)\n"
            + "  - Status: APPROVED\n"
            + "\n"
            + "The approved amount will be processed and disbursed according to your chosen method.\n\n"
            + "You can view the full details and disbursement status by logging into your account here:\n"
            + "%s\n\n"
            + "If you have any questions, please contact our support team.\n\n"
            + "Thank you for choosing %s.\n\n"
            + SINCERELY_TEXT,
            Objects.toString(customer.getName(), DEFAULT_CUSTOMER_NAME),
            claimId,
            Objects.toString(policy.getName(), "N/A"),
            Objects.toString(policy.getPolicyId(), "N/A"),
            claimId,
            Objects.toString(policy.getName(), "N/A"),
            Objects.toString(policy.getPolicyId(), "N/A"),
            LOGIN_URL,
            APP_NAME
        );
    }
    
    private String createRejectedEmailBody(CustomerDTO customer, Integer claimId, PolicyDTO policy) {
        return String.format(
            "Dear %s,\n\n"
            + "We regret to inform you that your claim (Claim ID: **%d**) "
            + "related to policy **%s (No: %s)** has been **REJECTED**.\n\n"
            + "**Claim Summary:**\n"
            + "  - Claim ID: %d\n"
            + "  - Policy: %s (No: %s)\n"
            + "  - Status: REJECTED\n"
            + "\n"
            + "We understand this news may be disappointing. You can view the full details of the decision and appeal options by logging into your account here:\n"
            + "%s\n\n"
            + "If you have any further questions or wish to appeal, please contact our support team.\n\n"
            + "Thank you for your understanding.\n\n"
            + SINCERELY_TEXT,
            Objects.toString(customer.getName(), DEFAULT_CUSTOMER_NAME),
            claimId,
            Objects.toString(policy.getName(), "N/A"),
            Objects.toString(policy.getPolicyId(), "N/A"),
            claimId,
            Objects.toString(policy.getName(), "N/A"),
            Objects.toString(policy.getPolicyId(), "N/A"),
            LOGIN_URL,
            APP_NAME
        );
    }

    //-----------------------------------------------------------------------------------

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
                .toList();

            if (expiringPolicies.isEmpty()) {
                logger.info("No policies are expiring in the next week.");
                return; 
            }
            logger.info("Found {} policies expiring in the next week.", expiringPolicies.size());

            for (PolicyDTO policy : expiringPolicies) {
                try {
                    CustomerDTO customer = getCustomerOrThrow(policy.getCustomerId());

                    String customerName = Objects.toString(customer.getName(), DEFAULT_CUSTOMER_NAME);
                    String policyNumber = Objects.toString(policy.getPolicyId(), "N/A");
                    String policyName = Objects.toString(policy.getName(), "Your Policy");
                    LocalDate expirationDate = policy.getExpiryDate();

                    String subject = String.format("Action Required: Your %s Policy (%s) is Expiring Soon!", policyName, policyNumber);
                    String body = String.format(
                        "Dear %s,\n\n"
                        + "This is a friendly reminder that your **%s** policy (Policy Number: **%s**) is set to expire on **%s**.\n\n"
                        + "To ensure continuous coverage and peace of mind, please renew your policy at your earliest convenience.\n\n"
                        + "You can easily renew your policy by logging into your %s account here:\n%s\n\n"
                        + "If you have already renewed or if you have any questions, please disregard this email or contact our support team for assistance.\n\n"
                        + "Thank you for choosing %s.\n\n"
                        + SINCERELY_TEXT,
                        customerName, policyName, policyNumber, expirationDate,
                        APP_NAME, LOGIN_URL, APP_NAME
                    );

                    sendEmail(customer.getEmail(), subject, body);
                    if (customer.getPhone() != null && customer.getPhone() != 0) {
                         sendSmsRenewalReminder(subject, "+91" + customer.getPhone());
                    }
                 
                    logger.info("Renewal reminder sent for policy {} to customer {}.", policyNumber, customer.getEmail());

                    // Add In-App Notification
                    InAppNotification inAppNotification = new InAppNotification();
                    inAppNotification.setSubject(subject);
                    inAppNotification.setType("Reminder");
                    inAppNotification.setDetails(body);
                    inAppNotification.setCustomerId(customer.getCustomerId());
                    addNotification(inAppNotification);

                } catch (MailException | CustomerNotFoundException e) {
                    logger.error("Failed to send renewal reminder for policy {}.", policy.getPolicyId(), e);
                }
            }
        } catch (Exception e) {
            logger.error("An error occurred while fetching policies for renewal reminders.", e);
        }
    }
 
    private void sendSmsRenewalReminder(String subject, String to) {
        try {
            Twilio.init(accountSid, authToken);
            Message message = Message.creator(new PhoneNumber(to), new PhoneNumber(trialNumber), subject).create();
            logger.info("Reminder SMS Sent: {} to {}", message.getSid(), to);
        } catch(Exception e) {
            logger.error("Failed to send renewal SMS to {}: {}", to, e.getMessage());
        }
    }

    //-----------------------------------------------------------------------------------

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
    
    //-----------------------------------------------------------------------------------

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
            sendEmail(to, subject, body);
            logger.info("Mail Sent Successfully to {}.", to);
            return "True";
        } catch (MailException e) {
            logger.error("Failed to send email to {}.", to, e);
            throw new EmailSendingException("Failed to send email to " + to, e);
        }
    }
    private ClaimDTO getClaimOrThrow(Integer claimId) throws ClaimNotFoundException {
        ResponseEntity<ClaimDTO> claimEntity = claimclient.getClaimById(claimId);
        if (!claimEntity.hasBody() || claimEntity.getBody() == null) {
            throw new ClaimNotFoundException("Claim with ID " + claimId + " not found.");
        }
        return claimEntity.getBody();
    }
    private PolicyDTO getPolicyOrThrow(Integer policyId) throws PolicyNotFoundException {
        ResponseEntity<PolicyDTO> policyEntity = policyclient.getPolicyById(policyId);
        if (!policyEntity.hasBody() || policyEntity.getBody() == null) {
            throw new PolicyNotFoundException("Policy with ID " + policyId + " not found.");
        }
        return policyEntity.getBody();
    }
    
    private AgentDTO getAgentOrThrow(Integer agentId) throws AgentNotFoundException {
        ResponseEntity<AgentDTO> agentEntity = agentclient.getAgentById(agentId);
        if (!agentEntity.hasBody() || agentEntity.getBody() == null) {
            throw new AgentNotFoundException("Agent with ID " + agentId + " not found.");
        }
        return agentEntity.getBody();
    }

    private CustomerDTO getCustomerOrThrow(Integer customerId) {
        CustomerDTO customer = customerclient.getCustomerById(customerId);
        if (customer == null) {
            throw new CustomerNotFoundException("Customer with ID " + customerId + " not found.");
        }
        return customer;
    }

    @Override
    public void sendAgentCred(AgentCredentialsDTO cred) throws AgentNotFoundException, EmailSendingException {
        if (cred == null || cred.getContactInfo() == null) {
            logger.error("Cannot send agent credentials email: AgentCredentialsDTO or agent email is null.");
            return;
        }

        try {
            String agentName = cred.getName();
            String agentEmail = cred.getContactInfo();
            String orgMail = cred.getOrgEmail();
            String password = cred.getPassword();
            
            System.out.println(agentName +" ### "+ agentEmail +" ### ");

            String subject = String.format("Welcome to %s! Your Agent Account Credentials", APP_NAME);
            String body = String.format(
                "Dear %s,\n\n"
                + "Welcome to the %s team! Your agent account has been successfully created. "
                + "You can now log in to the agent dashboard using the following credentials:\n\n"
                + "**Username:** %s\n"
                + "**Password:** %s\n\n"
                + "\n\n"
                + "You can access your dashboard here:\n"
                + "%s\n\n"
                + "If you have any questions, please do not hesitate to contact support.\n\n"
                + SINCERELY_TEXT,
                agentName, APP_NAME, orgMail, password, LOGIN_URL
            );

            sendEmail(agentEmail, subject, body);
            logger.info("Agent credentials email sent successfully to {} for agent ID {}.", agentEmail);
        } catch (MailException e) {
            logger.error("Failed to send agent credentials email to {}.", cred.getContactInfo(), e);
            throw new EmailSendingException("Failed to send agent credentials email for agent ID " + cred.getContactInfo(), e);
        }
    }

    public void addNotification(InAppNotification notification) {
        inappNotificationrepo.save(notification);
    }
    
    public void addReminderNotification(InAppNotification notification) {
        notification.setType("Reminder");
        inappNotificationrepo.save(notification);
    }

    public void deleteNotification(Integer id) {
        inappNotificationrepo.deleteById(id);
    }
    
    public List<InAppNotification> getNotificationsByAgentId(Integer agentId) {
        return inappNotificationrepo.findByAgentId(agentId);
    }

    public List<InAppNotification> getNotificationsByCustomerId(Integer customerId) {
        return inappNotificationrepo.findByCustomerId(customerId);
    }
}