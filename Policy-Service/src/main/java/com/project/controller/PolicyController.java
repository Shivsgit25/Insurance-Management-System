package com.project.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.project.model.Policy;
import com.project.model.PolicyAgent;
import com.project.service.PolicyService;

@RestController
@RequestMapping("/api/policies")
public class PolicyController {

    private static final Logger logger = LoggerFactory.getLogger(PolicyController.class);

    private final PolicyService policyService;

    public PolicyController(PolicyService policyService) {
        this.policyService = policyService;
    }

    @PostMapping("/create")
    public ResponseEntity<Policy> createPolicy(@RequestBody Policy request) {
        logger.info("Creating policy ");

        Policy createdPolicy = policyService.createPolicy(request);

        logger.debug("Created policy: {}", createdPolicy);
        return ResponseEntity.ok(createdPolicy);
    }

    @PutMapping("/{policyId}")
    public ResponseEntity<Policy> updatePolicy(
            @PathVariable Integer policyId,
            @RequestBody Policy updatedPolicy) {
        logger.info("Updating policy with ID={}", policyId);
        Policy policy = policyService.updatePolicy(policyId, updatedPolicy);
        logger.debug("Updated policy: {}", policy);
        return ResponseEntity.ok(policy);
    }

    @DeleteMapping("/{policyId}")
    public ResponseEntity<String> deletePolicy(@PathVariable Integer policyId) {
        logger.info("Deleting policy with ID={}", policyId);
        policyService.deletePolicy(policyId);
        logger.info("Policy with ID={} deleted successfully", policyId);
        return ResponseEntity.ok("Policy with ID " + policyId + " deleted successfully.");
    }

    @GetMapping("/{policyId}")
    public ResponseEntity<Policy> getPolicyById(@PathVariable Integer policyId) {
        logger.info("Fetching policy with ID={}", policyId);
        Policy policy = policyService.getPolicyById(policyId);
        logger.debug("Fetched policy: {}", policy);
        return ResponseEntity.ok(policy);
    }

    @GetMapping
    public ResponseEntity<List<Policy>> getAllPolicies() {
        logger.info("Fetching all policies");
        List<Policy> policies = policyService.getAllPolicies();
        logger.debug("Total policies fetched: {}", policies.size());
        return ResponseEntity.ok(policies);
    }

    @GetMapping("/getCustomerPolicyDetails/{customerId}")
    public List<Policy> getCollection(@PathVariable("customerId") Integer customerId){
        logger.info("Fetching all policies by customerId={}", customerId);
        List<Policy> policies = policyService.getallpoliciesbycustomerId(customerId);
        logger.debug("Policies fetched: {}", policies.size());
        return policies;
    }

    @GetMapping("/agentAll/{agentId}")
    public List<Policy> getPolicies(@PathVariable("agentId") Integer agentId){
        logger.info("Fetching all policies by agentId={}", agentId);
        List<Policy> policies = policyService.getallpoliciesbyagentId(agentId);
        logger.debug("Policies fetched: {}", policies.size());
        return policies;
    }

    @GetMapping("/getAgentDetails/{policyId}")
    public PolicyAgent getAgents(@PathVariable("policyId") Integer policyId) {
        logger.info("Fetching agent details for policyId={}", policyId);
        PolicyAgent agentDetails = policyService.getPolyAgentCombo(policyId);
        logger.debug("Fetched agent details: {}", agentDetails);
        return agentDetails;
    }
}
