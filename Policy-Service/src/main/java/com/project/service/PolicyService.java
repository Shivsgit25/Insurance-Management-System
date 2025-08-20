package com.project.service;

import java.util.List;

import com.project.model.Policy;
import com.project.model.PolicyAgent;

public interface PolicyService {
    Policy createPolicy(Policy policy);
    Policy updatePolicy(Integer policyId, Policy updatedPolicy);
    void deletePolicy(Integer policyId);
    Policy getPolicyById(Integer policyId);
    List<Policy> getAllPolicies();
	List<Policy> getallpoliciesbycustomerId(Integer customerId);
	List<Policy> getallpoliciesbyagentId(Integer agentId);
    PolicyAgent getPolyAgentCombo(Integer policyId);
}
