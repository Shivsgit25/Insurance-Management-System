package com.project.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.project.model.InappNotificationDTO;

@FeignClient(name = "NOTIFICATION-SERVICE", path = "/notify")
public interface InappNotificationClient {

	@PostMapping("/add")
    public void addNotification(@RequestBody InappNotificationDTO notification);

}
