package com.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

@RestController
@RequestMapping("/notify")
public class NotificationController {
	
	@Autowired
	NotificationService service;

	@PostMapping("/actemail")
	public String sendEmailNotification(@RequestBody EmailDTO emailRequest) throws EmailSendingException {
	    return service.sendActualEmail(emailRequest.getTo(), emailRequest.getSubject(), emailRequest.getBody());
	}
	
	//registered
	@PostMapping("/customerRegistered")
	public void customerRegisteredMail(@RequestBody CustomerDTO customer) throws EmailSendingException {
		service.sendRegisteredEmail(customer);
	}
	
	//policies opted
	@PostMapping("/policieopted")
	public void mailPoliciesOpted(@RequestBody PolicyDTO policy) throws EmailSendingException {
		service.sendMailPolicyOpted(policy);
	}
	
	//Claim FILLED
	@PostMapping("/claimfiled")
	public String claimFilled(@RequestBody ClaimDTO claim ) throws PolicyNotFoundException, AgentNotFoundException, EmailSendingException {
		return service.claimFilledEmail(claim);
	}
	
	//ClaimUpdationMail
	@PostMapping("/claimUpdated")
	public String claimUpdated(@RequestBody Integer claimId,@RequestBody ClaimDTO.Status status) throws PolicyNotFoundException, ClaimNotFoundException, EmailSendingException {
		return service.sendMailClaimUpdated(claimId,status);
	}


	 @PostMapping("/actsms")
	    public String sendSmsNotification(@RequestBody SmsDTO smsRequest) {
	       return service.sendActualSms(smsRequest.getTo(), smsRequest.getBody());
	 }

	 
}
