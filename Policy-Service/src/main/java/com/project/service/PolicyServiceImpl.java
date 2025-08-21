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

    /**
     * Creates a new policy and saves it to the database.
     *
     * @param policy the Policy object to be created
     * @return the saved Policy object
     */
    @Override
    public Policy createPolicy(Policy policy) {
        return policyRepository.save(policy);
    }

    /**
     * Updates an existing policy with new details.
     *
     * @param policyId the ID of the policy to update
     * @param updatedPolicy the Policy object containing updated values
     * @return the updated Policy object
     * @throws ResourceNotFoundException if the policy with the given ID does not exist
     */
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

    /**
     * Deletes a policy by its ID.
     *
     * @param policyId the ID of the policy to delete
     * @throws ResourceNotFoundException if the policy with the given ID does not exist
     */
    @Override
    public void deletePolicy(Integer policyId) {
        Policy policy = policyRepository.findById(policyId)
            .orElseThrow(() -> new ResourceNotFoundException(POLICY_ID_PREFIX + policyId + NOT_FOUND_MESSAGE));
        policyRepository.delete(policy);
    }

    /**
     * Retrieves all policies from the database.
     *
     * @return a list of all Policy objects
     */
    @Override
    public List<Policy> getAllPolicies() {
        return policyRepository.findAll();
    }

    /**
     * Retrieves a policy by its ID.
     *
     * @param policyId the ID of the policy to retrieve
     * @return the Policy object with the specified ID
     * @throws ResourceNotFoundException if the policy with the given ID does not exist
     */
    @Override
    public Policy getPolicyById(Integer policyId) {
        return policyRepository.findById(policyId)
            .orElseThrow(() -> new ResourceNotFoundException(POLICY_ID_PREFIX + policyId + NOT_FOUND_MESSAGE));
    }

    /**
     * Retrieves all policies associated with a specific customer.
     *
     * @param customerId the ID of the customer
     * @return a list of Policy objects linked to the customer
     */
    @Override
    public List<Policy> getallpoliciesbycustomerId(Integer customerId) {
        return policyRepository.findAllByCustomerId(customerId);
    }

    /**
     * Retrieves all policies associated with a specific agent.
     *
     * @param agentId the ID of the agent
     * @return a list of Policy objects linked to the agent
     */
    @Override
    public List<Policy> getallpoliciesbyagentId(Integer agentId) {
        return policyRepository.findAllByAgentId(agentId);
    }

    /**
     * Retrieves a combined object containing policy and its associated agent details.
     *
     * @param policyId the ID of the policy
     * @return a PolicyAgent object containing both policy and agent information
     * @throws ResourceNotFoundException if the policy with the given ID does not exist
     */
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
