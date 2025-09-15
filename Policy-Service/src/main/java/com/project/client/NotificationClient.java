package com.project.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.project.model.InappNotificationDTO;
import com.project.model.Policy;

@FeignClient(name = "NOTIFICATION-SERVICE", path = "/notify")
public interface NotificationClient {

	@PostMapping("/policieopted")
    public String mailPoliciesOpted(@RequestBody Policy policy);
	
	@PostMapping("/add")
    public void addNotification(@RequestBody InappNotificationDTO notification);

}
