package com.project.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.project.model.Policy;
import com.project.repository.PolicyRepository;

@Service
public class PolicyServiceImpl implements PolicyService {

	@Autowired
	PolicyRepository policyRepository;

	
//    @Autowired
//    CustomerClient customerClient;
	
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
	        }
	        return null;
	    }

	@Override
	public void deletePolicy(Integer policyId) {
		Optional<Policy> policyOpt = policyRepository.findById(policyId);
		if (policyOpt.isPresent()) {
		    Policy policy = policyOpt.get();
		    policyRepository.delete(policy);
		}
	}

	@Override
	public List<Policy> getAllPolicies() {
		return policyRepository.findAll();
	}

	@Override
	public Policy getPolicyById(Integer policyId) {
		return policyRepository.findById(policyId).orElse(null);
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
		List<Policy> policies = policyRepository.findAllByCustomerId(customerId);
		return policies;
	}

	@Override
	public List<Policy> getallpoliciesbyagentId(Integer agentId) {
		List<Policy> policies = policyRepository.findAllByAgentId(agentId);
		return policies;
	}



//    @Override
//	public PolicyCustomer getPoliciesByCustomerId(Integer customerId) {
//		Optional<Policy> optional = policyRepository.findById(customerId);
//		Policy policy = optional.get();
//		CustomerDTO customerdto = customerClient.getCustomerByPolicyId(policyId);
//		PolicyCustomer policyCustomer = new PolicyCustomer();
//		customerdto.
//		policyCustomer.setPolicy(policy);
//		return policyCustomer;
//
//	}
	
	

}




 