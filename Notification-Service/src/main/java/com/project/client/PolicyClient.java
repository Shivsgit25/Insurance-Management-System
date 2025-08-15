package com.project.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.project.DTO.PolicyDTO;


@FeignClient(name = "POLICY-SERVICE",path ="/api/policies")
public interface PolicyClient {

	@GetMapping("/{policyId}")
    public ResponseEntity<PolicyDTO> getPolicyById(@PathVariable Integer policyId);
	
	@GetMapping
    public ResponseEntity<List<PolicyDTO>> getAllPolicies();
	
}
