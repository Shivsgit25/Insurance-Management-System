package com.project.service;

import java.util.List;

import com.project.model.Policy;

public interface PolicyService {
    Policy createPolicy(Policy policy, Long customerId, Long agentId);
    Policy updatePolicy(Long policyId, Policy updatedPolicy);
    void deletePolicy(Long policyId);
    Policy getPolicyById(Long policyId);
    List<Policy> getAllPolicies();
    List<Policy> getPoliciesByCustomer(Long customerId);
    List<Policy> getPoliciesByAgent(Long agentId);
}
