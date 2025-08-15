package com.project.service;

import com.project.DTO.ClaimDTO;
import com.project.DTO.ClaimDTO.Status;
import com.project.DTO.CustomerDTO;
import com.project.DTO.PolicyDTO;

public interface NotificationService {

	String sendActualEmail(String to, String subject, String body);

	String sendActualSms(String to, String body);

	void sendRegisteredEmail(CustomerDTO customer);

	void sendMailPolicyOpted(PolicyDTO policy);

	String claimFilledEmail(ClaimDTO claim);

	String sendMailClaimUpdated(Integer claimId, Status status);

	void sendPolicyRenewalReminders();


}
