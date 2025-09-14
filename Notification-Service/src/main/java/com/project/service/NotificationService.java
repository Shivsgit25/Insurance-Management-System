package com.project.service;

import com.project.DTO.AgentCredentialsDTO;
import com.project.DTO.ClaimDTO;
import com.project.DTO.ClaimDTO.Status;
import com.project.DTO.CustomerDTO;
import com.project.DTO.PolicyDTO;
import com.project.exceptions.AgentNotFoundException;
import com.project.exceptions.ClaimNotFoundException;
import com.project.exceptions.EmailSendingException;
import com.project.exceptions.PolicyNotFoundException;

public interface NotificationService {

	String sendActualEmail(String to, String subject, String body) throws EmailSendingException;

	String sendActualSms(String to, String body);

	void sendRegisteredEmail(CustomerDTO customer) throws EmailSendingException;

	void sendMailPolicyOpted(PolicyDTO policy) throws EmailSendingException;

	void claimFilledEmail(ClaimDTO claim) throws PolicyNotFoundException, AgentNotFoundException, EmailSendingException;

	void sendMailClaimUpdated(Integer claimId, Status status) throws PolicyNotFoundException, ClaimNotFoundException, EmailSendingException;

	void sendPolicyRenewalReminders();

	void sendAgentCred(AgentCredentialsDTO cred) throws AgentNotFoundException , EmailSendingException;


}
