package com.project.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    /**
     * Creates a new policy.
     *
     * @param request the Policy object containing policy details
     * @return ResponseEntity containing the created Policy
     */
    @PostMapping("/create")
    public ResponseEntity<Policy> createPolicy(@RequestBody Policy request) {
        logger.info("Creating policy ");
        Policy createdPolicy = policyService.createPolicy(request);
        logger.debug("Created policy: {}", createdPolicy);
        return ResponseEntity.ok(createdPolicy);
    }

    /**
     * Updates an existing policy by its ID.
     *
     * @param policyId the ID of the policy to update
     * @param updatedPolicy the updated Policy object
     * @return ResponseEntity containing the updated Policy
     */
    @PutMapping("/{policyId}")
    public ResponseEntity<Policy> updatePolicy(
            @PathVariable Integer policyId,
            @RequestBody Policy updatedPolicy) {
        logger.info("Updating policy with ID={}", policyId);
        Policy policy = policyService.updatePolicy(policyId, updatedPolicy);
        logger.debug("Updated policy: {}", policy);
        return ResponseEntity.ok(policy);
    }

    /**
     * Deletes a policy by its ID.
     *
     * @param policyId the ID of the policy to delete
     * @return ResponseEntity containing a confirmation message
     */
    @DeleteMapping("/{policyId}")
    public ResponseEntity<String> deletePolicy(@PathVariable Integer policyId) {
        logger.info("Deleting policy with ID={}", policyId);
        policyService.deletePolicy(policyId);
        logger.info("Policy with ID={} deleted successfully", policyId);
        return ResponseEntity.ok("Policy with ID " + policyId + " deleted successfully.");
    }

    /**
     * Retrieves a policy by its ID.
     *
     * @param policyId the ID of the policy to retrieve
     * @return ResponseEntity containing the requested Policy
     */
    @GetMapping("/{policyId}")
    public ResponseEntity<Policy> getPolicyById(@PathVariable Integer policyId) {
        logger.info("Fetching policy with ID={}", policyId);
        Policy policy = policyService.getPolicyById(policyId);
        logger.debug("Fetched policy: {}", policy);
        return ResponseEntity.ok(policy);
    }

    /**
     * Retrieves all policies.
     *
     * @return ResponseEntity containing a list of all Policy objects
     */
    @GetMapping
    public ResponseEntity<List<Policy>> getAllPolicies() {
        logger.info("Fetching all policies");
        List<Policy> policies = policyService.getAllPolicies();
        logger.debug("Total policies fetched: {}", policies.size());
        return ResponseEntity.ok(policies);
    }

    /**
     * Retrieves all policies associated with a specific customer.
     *
     * @param customerId the ID of the customer
     * @return List of Policy objects linked to the customer
     */
    @GetMapping("/getCustomerPolicyDetails/{customerId}")
    public List<Policy> getCollection(@PathVariable("customerId") Integer customerId){
        logger.info("Fetching all policies by customerId={}", customerId);
        List<Policy> policies = policyService.getallpoliciesbycustomerId(customerId);
        logger.debug("Policies fetched: {}", policies.size());
        return policies;
    }

    /**
     * Retrieves all policies associated with a specific agent.
     *
     * @param agentId the ID of the agent
     * @return List of Policy objects linked to the agent
     */
    @GetMapping("/agentAll/{agentId}")
    public List<Policy> getPolicies(@PathVariable("agentId") Integer agentId){
        logger.info("Fetching all policies by agentId={}", agentId);
        List<Policy> policies = policyService.getallpoliciesbyagentId(agentId);
        logger.debug("Policies fetched: {}", policies.size());
        return policies;
    }

    /**
     * Retrieves agent details associated with a specific policy.
     *
     * @param policyId the ID of the policy
     * @return PolicyAgent object containing agent details
     */
    @GetMapping("/getAgentDetails/{policyId}")
    public PolicyAgent getAgents(@PathVariable("policyId") Integer policyId) {
        logger.info("Fetching agent details for policyId={}", policyId);
        PolicyAgent agentDetails = policyService.getPolyAgentCombo(policyId);
        logger.debug("Fetched agent details: {}", agentDetails);
        return agentDetails;
    }

}
