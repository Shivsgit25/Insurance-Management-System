package com.project.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.project.DTO.InappNotificationDTO;
import com.project.model.Claim;

@FeignClient(name = "NOTIFICATION-SERVICE", path = "/notify")
public interface NotificationClient {
	
	@PostMapping("/add")
    public void addNotification(@RequestBody InappNotificationDTO notification);


	@PostMapping("/claimfiled")
	public String claimFilled(@RequestBody Claim claim);

	@PostMapping("/claimUpdated")
	public String claimUpdated(@RequestParam("claimID") Integer claimId, @RequestParam("status") Claim.Status status);

}
