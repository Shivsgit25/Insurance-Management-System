package com.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.DTO.CustomerDTO;
import com.project.DTO.EmailDTO;
import com.project.DTO.PolicyDTO;
import com.project.DTO.SmsDTO;
import com.project.service.NotificationService;

@RestController
@RequestMapping("/notify")
public class NotificationController {
	
	@Autowired
	NotificationService service;
	
//	EMailS##################################################
	
	@PostMapping("/actemail")
	public String sendEmailNotification(@RequestBody EmailDTO emailRequest) {
	    return service.sendActualEmail(emailRequest.getTo(), emailRequest.getSubject(), emailRequest.getBody());
	}
	
	//registered
	@PostMapping("/customerRegistered")
	public String customerRegisteredMail(@RequestBody CustomerDTO customer) {
		service.sendRegisteredEmail(customer);
		return "Success";
	}
	
	//policies opted
	@PostMapping("/policieopted")
	public String mailPoliciesOpted(@RequestBody PolicyDTO policy) {
		service.sendMailPolicyOpted(policy);
		return "Success";
	}
	
//	SMS######################################################
	
	 @PostMapping("/actsms")
	    public String sendSmsNotification(@RequestBody SmsDTO smsRequest) {
	       return service.sendActualSms(smsRequest.getTo(), smsRequest.getBody());
	 }
	 
//	 INAPP###################################################
	 
	 
}
