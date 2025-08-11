package com.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.service.NotificationService;

@RestController
@RequestMapping("/notification")
public class NotificationController {

//	@Autowired 
//	NotificationService service;
//	
//	
	@GetMapping("/test")
	public String test() {
		return "Hello there";
	}
	
	
	
}
