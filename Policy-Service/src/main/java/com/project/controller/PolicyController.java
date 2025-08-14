package com.project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.model.Policy;
import com.project.service.PolicyService;

@RestController
@RequestMapping("/api/policies")
public class PolicyController {

    @Autowired
    private PolicyService policyService;

    // Create a new policy
    @PostMapping("/customer/{customerId}/agent/{agentId}")
    public ResponseEntity<Policy> createPolicy(
            @PathVariable Integer customerId,
            @PathVariable Integer agentId,
            @RequestBody Policy policy) {
        Policy createdPolicy = policyService.createPolicy(policy, customerId, agentId);
        return ResponseEntity.ok(createdPolicy);
    }

    // Update an existing policy
    @PutMapping("/{policyId}")
    public ResponseEntity<Policy> updatePolicy(
            @PathVariable Integer policyId,
            @RequestBody Policy updatedPolicy) {
        Policy policy = policyService.updatePolicy(policyId, updatedPolicy);
        return ResponseEntity.ok(policy);
    }

    // Delete a policy
    @DeleteMapping("/{policyId}")
    public ResponseEntity<String> deletePolicy(@PathVariable Integer policyId) {
        policyService.deletePolicy(policyId);
        return ResponseEntity.ok("Policy with ID " + policyId + " deleted successfully.");
    }

    // Get a policy by ID
    @GetMapping("/{policyId}")
    public ResponseEntity<Policy> getPolicyById(@PathVariable Integer policyId) {
        Policy policy = policyService.getPolicyById(policyId);
        return ResponseEntity.ok(policy);
    }

    // Get all policies
    @GetMapping
    public ResponseEntity<List<Policy>> getAllPolicies() {
        List<Policy> policies = policyService.getAllPolicies();
        return ResponseEntity.ok(policies);
    }

    // Get policies by customer ID
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Policy>> getPoliciesByCustomer(@PathVariable Integer customerId) {
        List<Policy> policies = policyService.getPoliciesByCustomer(customerId);
        return ResponseEntity.ok(policies);
    }

    // Get policies by agent ID
    @GetMapping("/agent/{agentId}")
    public ResponseEntity<List<Policy>> getPoliciesByAgent(@PathVariable Integer agentId) {
        List<Policy> policies = policyService.getPoliciesByAgent(agentId);
        return ResponseEntity.ok(policies);
    }
    
    
    @GetMapping("/test/{customerId}") // Corrected path to match PolicyController
    public List<Policy> getCollection(@PathVariable("customerId") Integer customerId){
    	return policyService.getallpoliciesbycustomerId(customerId);
    } 
}
