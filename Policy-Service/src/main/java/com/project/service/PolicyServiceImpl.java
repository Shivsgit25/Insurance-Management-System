package com.project.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.project.client.AgentClient;
import com.project.exception.ResourceNotFoundException;
import com.project.model.AgentDTO;
import com.project.model.Policy;
import com.project.model.PolicyAgent;
import com.project.repository.PolicyRepository;

@Service
public class PolicyServiceImpl implements PolicyService {

    private static final String POLICY_ID_PREFIX = "Policy with ID ";
    private static final String NOT_FOUND_MESSAGE = " not found.";

    private final PolicyRepository policyRepository;
    private final AgentClient agentclient;

    public PolicyServiceImpl(PolicyRepository policyRepository, AgentClient agentclient) {
        this.policyRepository = policyRepository;
        this.agentclient = agentclient;
    }

    @Override
	public Policy createPolicy(Policy policy) {
		return policyRepository.save(policy);
	}

    @Override
    public Policy updatePolicy(Integer policyId, Policy updatedPolicy) {
        Policy existingPolicy = policyRepository.findById(policyId)
            .orElseThrow(() -> new ResourceNotFoundException(POLICY_ID_PREFIX + policyId + NOT_FOUND_MESSAGE));

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
            .orElseThrow(() -> new ResourceNotFoundException(POLICY_ID_PREFIX + policyId + NOT_FOUND_MESSAGE));
        policyRepository.delete(policy);
    }

    @Override
    public List<Policy> getAllPolicies() {
        return policyRepository.findAll();
    }

    @Override
    public Policy getPolicyById(Integer policyId) {
        return policyRepository.findById(policyId)
            .orElseThrow(() -> new ResourceNotFoundException(POLICY_ID_PREFIX + policyId + NOT_FOUND_MESSAGE));
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
        Policy policy = policyRepository.findById(policyId)
            .orElseThrow(() -> new ResourceNotFoundException(POLICY_ID_PREFIX + policyId + NOT_FOUND_MESSAGE));
        PolicyAgent policyagent = new PolicyAgent();
        policyagent.setPolicy(policy);
        policyagent.setAgent(agentdto);
        return policyagent;
    }

	
}
