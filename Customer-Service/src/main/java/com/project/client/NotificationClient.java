package com.project.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.project.model.Customer;


@FeignClient
(name = "NOTIFICATION-SERVICE",path ="/notify")
public interface NotificationClient {
	@PostMapping("/customerRegistered")
	public void customerRegisteredMail(@RequestBody Customer customer);
}
