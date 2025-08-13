package com.project.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.exception.ResourceNotFoundException;
import com.project.model.Policy;
import com.project.repository.PolicyRepository;

@Service
public class PolicyServiceImpl implements PolicyService {

	@Autowired
	private PolicyRepository policyRepository;

//
//    @Autowired
//    private CustomerRepository customerRepository;
//
//    @Autowired
//    private AgentRepository agentRepository;
//
//    @Override
//    public Policy createPolicy(Policy policy, Long customerId, Long agentId) {
//        Customer customer = customerRepository.findById(customerId)
//                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
//
//        Agent agent = agentRepository.findById(agentId)
//                .orElseThrow(() -> new ResourceNotFoundException("Agent not found"));
//
//        policy.setCustomer(customer);
//        policy.setAgent(agent);
//
//        return policyRepository.save(policy);
//    }
//    
	@Override
	public Policy createPolicy(Policy policy, Integer customerId, Integer agentId) {
		policy.setCustomerId(customerId);
		policy.setAgentId(agentId);
		return policyRepository.save(policy);
	}

	@Override
	public Policy updatePolicy(Integer policyId, Policy updatedPolicy) {
		Optional<Policy> existingPolicyOpt = policyRepository.findById(policyId);
		if (existingPolicyOpt.isPresent()) {
			Policy existingPolicy = existingPolicyOpt.get();
			existingPolicy.setName(updatedPolicy.getName());
			existingPolicy.setPremiumAmount(updatedPolicy.getPremiumAmount());
			existingPolicy.setCoverageDetails(updatedPolicy.getCoverageDetails());
			existingPolicy.setValidityPeriod(updatedPolicy.getValidityPeriod());
			existingPolicy.setCustomerId(updatedPolicy.getCustomerId());
			existingPolicy.setAgentId(updatedPolicy.getAgentId());
			return policyRepository.save(existingPolicy);
		} else {
			throw new RuntimeException("Policy not found with ID: " + policyId);

		}

	}

	@Override
	public void deletePolicy(Integer policyId) {
		Policy policy = policyRepository.findById(policyId)
				.orElseThrow(() -> new ResourceNotFoundException("Policy not found"));
		policyRepository.delete(policy);

	}

	@Override
	public List<Policy> getAllPolicies() {
		return policyRepository.findAll();
	}

	@Override
	public Policy getPolicyById(Integer policyId) {
		return policyRepository.findById(policyId).orElseThrow(() -> new ResourceNotFoundException("Policy not found"));
	}

	@Override
	public List<Policy> getPoliciesByCustomer(Integer customerId) {
		return policyRepository.findByCustomerId(customerId);
	}

	@Override
	public List<Policy> getPoliciesByAgent(Integer agentId) {
		return policyRepository.findByAgentId(agentId);

	}

}
