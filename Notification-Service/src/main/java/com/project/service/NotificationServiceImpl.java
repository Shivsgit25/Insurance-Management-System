package com.project.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
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
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@Service
public class NotificationServiceImpl implements NotificationService {

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

		String body = "Dear " + customerName + ",\n\n" + "Welcome to " + appName
				+ "! We are thrilled to have you as a new member.\n\n"
				+ "Your registration was successful, and your account is now active.\n\n"
				+ "You can now log in to your dashboard to manage your policies, view claims, and access exclusive features:\n"
				+ loginUrl + "\n\n"
				+ "If you have any questions or need assistance, please do not hesitate to contact our support team.\n\n"
				+ "Thank you for choosing " + appName + ".\n\n" + "Sincerely,\n" + "The " + appName + " Team";

		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(senderEmail); // Correctly uses the injected senderEmail
		message.setTo(to);
		message.setSubject(subject);
		message.setText(body);
		javaMailSender.send(message);
		System.out.println("Mail Sent Successfully!!!");
	}

	// Policy Selection mail

	@Autowired
	CustomerClient customerclient;

	@Override
	@Async
	public void sendMailPolicyOpted(PolicyDTO policy) {
//    	System.out.println(policy.getCustomerId()+"##########################################################################");
		CustomerDTO customer = customerclient.getCustomerById(policy.getCustomerId());
//    	System.out.println(customer.getName()+"############################################################");

		String to = customer.getEmail();
		String customerName = customer.getName();
		String policyName = policy.getName();
		String policyDetails = policy.getCoverageDetails(); // Can be a summary or a link

		String subject = "Congratulations! Your " + policyName + " is Active with " + appName + "! 🎉";

		String body = "Dear " + customerName + ",\n\n" + "We're excited to confirm that your application for the **"
				+ policyName
				+ "** policy has been successfully processed. Your new policy is now active and ready to provide you with peace of mind!\n\n"
				+ "Here’s a summary of your new policy:\n" + "  - **Policy Name:** " + policyName + "\n"
				+ "  - **Key Details:** "
				+ (policyDetails != null && !policyDetails.isEmpty() ? policyDetails
						: "Please log in for full details.")
				+ "\n\n"
				+ "You can access all the specifics, download your policy documents, and manage your coverage anytime by logging into your "
				+ appName + " account here:\n" + loginUrl + "\n\n"
				+ "We are committed to providing you with excellent service and comprehensive coverage. If you have any questions or need further assistance, please don't hesitate to reach out to our dedicated support team.\n\n"
				+ "Thank you for choosing " + appName + ".\n\n" + "Sincerely,\n" + "The " + appName + " Team";

		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("your_email@gmail.com"); // Sender email, can also be retrieved from properties
		message.setTo(to);
		message.setSubject(subject);
		message.setText(body);
		javaMailSender.send(message);
		System.out.println("MAIl SENT#################");
		System.out.println("Policies Opted Successfully!!");
	}

	// Claim Filled MAil Sent

	@Autowired
	PolicyClient policyclient;
	@Autowired
	AgentClient agentclient;

	@Override
	public String claimFilledEmail(ClaimDTO claim) {
		// policy
		ResponseEntity<PolicyDTO> opt = policyclient.getPolicyById(claim.getPolicyId());
		PolicyDTO policy = opt.getBody();
		Integer policyNumber = policy.getPolicyId();
		String policyName = policy.getName();
		// agent
		ResponseEntity<AgentDTO> optagent = agentclient.getAgentById(claim.getAgentId());
		AgentDTO assignedAgent = optagent.getBody();
		String agentName = assignedAgent.getName();
		String agentEmail = assignedAgent.getContactInfo();
		// customer
		CustomerDTO customer = customerclient.getCustomerById(claim.getCustomerId());
		String to = customer.getEmail();
		String customerName = customer.getName();
		Integer claimId = claim.getClaimId();
		Double claimAmount = claim.getClaimAmount();

		// --- Subject for Claim Filed Email ---
		String subject = "Confirmation: Your Claim #" + claimId + " Has Been Filed with " + appName + " 🎉";

		// --- Body for Claim Filed Email (using simple concatenation) ---
		String body = "Dear " + customerName + ",\n\n" + "We confirm that your claim (Claim ID: **" + claimId
				+ "**) related to policy **" + policyNumber + "** has been successfully filed with " + appName + ".\n\n"
				+ "**Claim Details Submitted:**\n" + "  - Claim ID: " + claimId + "\n" + "  - Associated Policy: "
				+ policyNumber + "\n" + "  - Claimed Amount: ₹"
				+ (claimAmount != null ? String.format("%,.2f", claimAmount) : "N/A") + "\n\n"
				+ "Our team is now reviewing your claim. You can track the real-time status of your claim and find all related documents by logging into your account here:\n"
				+ loginUrl + "\n\n";

		if (assignedAgent != null && agentName != null) {
			body += "Your dedicated agent, " + agentName + ", will be assisting you with this claim.\n";
			body += "You can reach them at: ";
			if (agentEmail != null) {
				body += agentEmail;
			}
		} else {
			body += "Our support team will be in touch with any updates.\n\n";
		}

		body += "If you have any questions, please contact our support team.\n\n" + "Thank you for choosing " + appName
				+ ".\n\n" + "Sincerely,\n" + "The " + appName + " Team";

		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("your_email@gmail.com"); // Sender email, can also be retrieved from properties
		message.setTo(to);
		message.setSubject(subject);
		message.setText(body);
		javaMailSender.send(message);
		System.out.println("MAIl SENT#################");
		System.out.println("Claim Filed Successfully!!");

		// --- Subject for Agent Email ---
		subject = "New Claim Assigned: Claim #" + claimId + " for Policy " + policyNumber;

		// --- Body for Agent Email ---
		body = "Dear " + agentName + ",\n\n"
				+ "A new claim has been filed and assigned to you for review and processing.\n\n"
				+ "**Claim Details:**\n" + "  - Claim ID: " + claimId + "\n" + "  - Associated Policy: " + policyName
				+ " (Policy No: " + policyNumber + ")\n" + "  - Claimed Amount: ₹"
				+ (claimAmount != null ? String.format("%,.2f", claimAmount) : "N/A") + "\n\n"
				+ "**Customer Details:**\n" + "  - Customer Name: " + customerName + "\n" + "  - Customer Email: " + to
				+ "\n\n" + "Please log in to your agent dashboard at " + loginUrl
				+ " to review the full claim details and take necessary action.\n\n" + "Thank you,\n" + "The " + appName
				+ " Team";

		message.setFrom("your_email@gmail.com"); // Sender email, can also be retrieved from properties
		message.setTo(agentEmail);
		message.setSubject(subject);
		message.setText(body);
		javaMailSender.send(message);
		System.out.println("MAIl SENT#################");
		System.out.println("Claim Filed Successfully!!");

		return "Mail Sent To Customer and Agent";
	}

	@Autowired
	ClaimClient claimclient;

	public String sendMailClaimUpdated(Integer claimId, ClaimDTO.Status status) {

		ResponseEntity<ClaimDTO> claimentitiy = claimclient.getClaimById(claimId);
		ClaimDTO claim = claimentitiy.getBody();

		CustomerDTO customer = customerclient.getCustomerById(claim.getCustomerId());
		String customerName = customer.getName();
		String customerEmail = customer.getEmail();

		ResponseEntity<PolicyDTO> policyentity = policyclient.getPolicyById(claim.getPolicyId());
		PolicyDTO policy = policyentity.getBody();
		String policyName = policy.getName();
		Integer policyNumber = policy.getPolicyId();
		String subject = "", body = "";
		if (status == ClaimDTO.Status.APPROVED) {
			subject = "Great News! Your Claim #" + claimId + " Has Been Approved! 🎉";
			body = "Dear " + customerName + ",\n\n" + "We are delighted to inform you that your claim (Claim ID: **"
					+ claimId + "**) " + "related to policy **" + policyName + " (No: " + policyNumber
					+ ")** has been **APPROVED**.\n\n" + "**Claim Summary:**\n" + "  - Claim ID: " + claimId + "\n"
					+ "  - Policy: " + policyName + " (No: " + policyNumber + ")\n" + "  - Status: APPROVED\n";
			body += "\n" + "The approved amount will be processed and disbursed according to your chosen method.\n\n"
					+ "You can view the full details and disbursement status by logging into your account here:\n"
					+ loginUrl + "\n\n" + "If you have any questions, please contact our support team.\n\n" + 
					"Thank you for choosing " + appName + ".\n\n" + "Sincerely,\n" + "The " + appName + " Team";

		} else {
			subject = "Important Update: Your Claim #" + claimId + " Status - Rejected";
			body = "Dear " + customerName + ",\n\n" + "We regret to inform you that your claim (Claim ID: **" + claimId
					+ "**) " + "related to policy **" + policyName + " (No: " + policyNumber
					+ ")** has been **REJECTED**.\n\n" + "**Claim Summary:**\n" + "  - Claim ID: " + claimId + "\n"
					+ "  - Policy: " + policyName + " (No: " + policyNumber + ")\n" + "  - Status: REJECTED\n";
			body += "\n"
					+ "We understand this news may be disappointing. You can view the full details of the decision and appeal options by logging into your account here:\n"
					+ loginUrl + "\n\n"
					+ "If you have any further questions or wish to appeal, please contact our support team.\n\n" + 
					"Thank you for your understanding.\n\n" + "Sincerely,\n" + "The " + appName + " Team";
		}

		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("your_email@gmail.com"); // Sender email, can also be retrieved from properties
		message.setTo(customerEmail);
		message.setSubject(subject);
		message.setText(body);
		javaMailSender.send(message);
		System.out.println("MAIl SENT#################");
		System.out.println("Claim Filed Successfully!!");

		return "Mail Send to Customer For Claim Updation";
	}

	// Remainders

	@Async // Make it asynchronous
	@Override // Implement the method from the interface
	public void sendPolicyRenewalReminders() {
		// 1. Get all policies
		ResponseEntity<List<PolicyDTO>> allPoliciesentity = policyclient.getAllPolicies();
		List<PolicyDTO> allPolicies = allPoliciesentity.getBody();
		LocalDate today = LocalDate.now();
		LocalDate oneWeekFromNow = today.plusWeeks(1);

		// 2. Filter policies expiring within a week
		List<PolicyDTO> expiringPolicies = allPolicies.stream()
				.filter(policy -> policy.getExpiryDate() != null && policy.getExpiryDate().isAfter(today)
						&& policy.getExpiryDate().isBefore(oneWeekFromNow.plusDays(1))) // Inclusive of one week
				.collect(Collectors.toList());

		// 3. Loop through expiring policies and send emails
		for (PolicyDTO policy : expiringPolicies) {
			String to = null;
			String customerName = "Valued Customer";
			String policyNumber = policy.getPolicyId() != null ? policy.getPolicyId().toString() : "N/A";
			String policyName = policy.getName() != null ? policy.getName() : "Your Policy";
			LocalDate expirationDate = policy.getExpiryDate();

			// Get customer details for each policy
			CustomerDTO customer = customerclient.getCustomerById(policy.getCustomerId());
			to = customer.getEmail();
			customerName = customer.getName() != null ? customer.getName() : "Valued Customer";

			// --- Subject for Renewal Reminder ---
			String subject = "Action Required: Your " + policyName + " Policy (" + policyNumber
					+ ") is Expiring Soon! ⏰";

			// --- Body for Renewal Reminder ---
			String body = "Dear " + customerName + ",\n\n" + "This is a friendly reminder that your **" + policyName
					+ "** policy (Policy Number: **" + policyNumber + "**) is set to expire on **" + expirationDate
					+ "**.\n\n"
					+ "To ensure continuous coverage and peace of mind, please renew your policy at your earliest convenience.\n\n"
					+ "You can easily renew your policy by logging into your " + appName + " account here:\n" + loginUrl
					+ "\n\n"
					+ "If you have already renewed or if you have any questions, please disregard this email or contact our support team for assistance.\n\n"
					+ "Thank Data for choosing " + appName + ".\n\n" + "Sincerely,\n" + "The " + appName + " Team";

			SimpleMailMessage message = new SimpleMailMessage();
			message.setFrom("your_email@gmail.com"); // Sender email, can also be retrieved from properties
			message.setTo(to);
			message.setSubject(subject);
			message.setText(body);
			javaMailSender.send(message);

		}

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

		Message message = Message.creator(new PhoneNumber(to), new PhoneNumber(trialNumber), body).create();

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
