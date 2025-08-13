package com.project.service;

import java.util.List;

import com.project.model.Policy;
import com.project.model.PolicyCustomer;

public interface PolicyService {
    Policy createPolicy(Policy policy, Integer customerId, Integer agentId);
    Policy updatePolicy(Integer policyId, Policy updatedPolicy);
    void deletePolicy(Integer policyId);
    Policy getPolicyById(Integer policyId);
    List<Policy> getAllPolicies();
    List<Policy> getPoliciesByCustomer(Integer customerId);
    List<Policy> getPoliciesByAgent(Integer agentId);
	PolicyCustomer getPoliciesByCustomerId(Integer customerId);
	List<Policy> getallpoliciesbycustomerId(Integer customerId);
}
