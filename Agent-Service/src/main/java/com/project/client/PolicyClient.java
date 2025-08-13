package com.project.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.project.model.Policy;

@FeignClient(name="Policy-Service", path="/policies")
public interface PolicyClient {
	@GetMapping("/policies/{policyId}")
	Policy getPolicyById(@PathVariable Integer policyId);
	
}
