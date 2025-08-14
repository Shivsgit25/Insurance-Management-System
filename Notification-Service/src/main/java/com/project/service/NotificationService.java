package com.project.service;

import com.project.DTO.CustomerDTO;
import com.project.DTO.PolicyDTO;

public interface NotificationService {

	String sendActualEmail(String to, String subject, String body);

	String sendActualSms(String to, String body);

	void sendRegisteredEmail(CustomerDTO customer);

	void sendMailPolicyOpted(PolicyDTO policy);


}
