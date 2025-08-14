package com.project.service;

import java.util.List;

import com.project.model.Policy;

public interface PolicyService {
    Policy createPolicy(Policy policy, Integer customerId, Integer agentId);
    Policy updatePolicy(Integer policyId, Policy updatedPolicy);
    void deletePolicy(Integer policyId);
    Policy getPolicyById(Integer policyId);
    List<Policy> getAllPolicies();
    List<Policy> getPoliciesByCustomer(Integer customerId);
    List<Policy> getPoliciesByAgent(Integer agentId);
	List<Policy> getallpoliciesbycustomerId(Integer customerId);
	List<Policy> getallpoliciesbyagentId(Integer agentId);
}
