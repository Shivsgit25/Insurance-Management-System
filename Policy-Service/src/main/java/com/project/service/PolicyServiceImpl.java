package com.project.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.client.AgentClient;
import com.project.exception.ResourceNotFoundException;
import com.project.model.AgentDTO;
import com.project.model.Policy;
import com.project.model.PolicyAgent;
import com.project.repository.PolicyRepository;

@Service
public class PolicyServiceImpl implements PolicyService {

    @Autowired
    private PolicyRepository policyRepository;
    @Autowired
    private AgentClient agentclient;

    @Override
    public Policy createPolicy(Policy policy, Integer customerId, Integer agentId) {
        policy.setCustomerId(customerId);
        policy.setAgentId(agentId);
        return policyRepository.save(policy);
    }

    @Override
    public Policy updatePolicy(Integer policyId, Policy updatedPolicy) {
        Policy existingPolicy = policyRepository.findById(policyId)
            .orElseThrow(() -> new ResourceNotFoundException("Policy with ID " + policyId + " not found."));

        existingPolicy.setName(updatedPolicy.getName());
        existingPolicy.setPremiumAmount(updatedPolicy.getPremiumAmount());
        existingPolicy.setCoverageDetails(updatedPolicy.getCoverageDetails());
        existingPolicy.setValidityPeriod(updatedPolicy.getValidityPeriod());
        existingPolicy.setCustomerId(updatedPolicy.getCustomerId());
        existingPolicy.setAgentId(updatedPolicy.getAgentId());

        return policyRepository.save(existingPolicy);
    }

    @Override
    public void deletePolicy(Integer policyId) {
        Policy policy = policyRepository.findById(policyId)
            .orElseThrow(() -> new ResourceNotFoundException("Policy with ID " + policyId + " not found."));
        policyRepository.delete(policy);
    }

    @Override
    public List<Policy> getAllPolicies() {
        return policyRepository.findAll();
    }

    @Override
    public Policy getPolicyById(Integer policyId) {
        return policyRepository.findById(policyId)
            .orElseThrow(() -> new ResourceNotFoundException("Policy with ID " + policyId + " not found."));
    }

    @Override
    public List<Policy> getPoliciesByCustomer(Integer customerId) {
        return policyRepository.findByCustomerId(customerId);
    }

    @Override
    public List<Policy> getPoliciesByAgent(Integer agentId) {
        return policyRepository.findByAgentId(agentId);
    }

    @Override
    public List<Policy> getallpoliciesbycustomerId(Integer customerId) {
        return policyRepository.findAllByCustomerId(customerId);
    }

    @Override
    public List<Policy> getallpoliciesbyagentId(Integer agentId) {
        return policyRepository.findAllByAgentId(agentId);
    }

	@Override
	public PolicyAgent getPolyAgentCombo(Integer policyId) {
	        List<AgentDTO> agentdto = agentclient.getAgents(policyId);
	        Optional<Policy> opt = policyRepository.findById(policyId);
	        PolicyAgent policyagent = new PolicyAgent();
	        Policy policy = opt.get();
	        policyagent.setPolicy(policy);
	        policyagent.setAgent(agentdto);
	        return policyagent;
	}

}
