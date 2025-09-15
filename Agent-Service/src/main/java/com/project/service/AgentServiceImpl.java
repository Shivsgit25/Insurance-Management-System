package com.project.service;
 
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.project.AgentServiceApplication;
import com.project.client.ClaimClient;
import com.project.client.CustomerClient;
import com.project.client.PolicyClient;
import com.project.exception.InvalidCredentialsException;
import com.project.exception.ResourceNotFoundException;
import com.project.model.Agent;
import com.project.model.AgentClaim;
import com.project.model.AgentPolicy;
import com.project.model.ClaimDTO;
import com.project.model.CustomerDTO;
import com.project.model.PolicyDTO;
import com.project.repository.AgentRepository;


@Service
public class AgentServiceImpl implements AgentService {
	
	private AgentRepository repo;
	PolicyClient policyclient;
	CustomerClient customerclient;
	ClaimClient claimclient;
	
	AgentServiceImpl(AgentRepository repo, PolicyClient policyClient , ClaimClient claimClient, CustomerClient customerClient, AgentServiceApplication agentServiceApplication){
		this.claimclient =claimClient;
		this.customerclient = customerClient;
		this.policyclient = policyClient;
		this.repo = repo;
	}
	
	private static final Logger logger = LoggerFactory.getLogger(AgentServiceImpl.class);

	private static final String AGENT_ALREADY_EXISTS_MESSAGE = "Agent already exist with this email";
    
	/**
	 * Creates a new agent and saves it to the database.
	 *
	 * @param agent The Agent object containing details to be persisted.
	 * @return A confirmation message indicating successful creation.
	 */
	@Override
	public String createAgent(Agent agent) {
		
		logger.info("Creating agent with ID: {}", agent.getAgentId());
//		if(repo.findByContactInfo(agent.getContactInfo()) != null) {
//			throw new AgentAlreadyExistsException(String.format(AGENT_ALREADY_EXISTS_MESSAGE, agent.getContactInfo()));
//			
//		}
		
	    repo.save(agent);
	    logger.debug("Agent Saved: {}",agent);
	    return "Agent saved";
	    
	}
	/**
	 * Retrieves all agents from the repository.
	 *
	 * @return A list of all Agent objects.
	 */
 
	@Override
	public List<Agent> getAllAgents() {
		logger.info("Fetching all agents");
		
		return repo.findAll();
		
	
	}
	
	/**
	 * Fetches an agent by their unique ID.
	 *
	 * @param agentId The ID of the agent to retrieve.
	 * @return An Optional containing the Agent if found, or empty if not.
	 */
	
    @Override
    public Optional<Agent> getAgentById(Integer agentId){
    	logger.info("Fetching agent by ID: {}",agentId);
    	return repo.findById(agentId);
    	
    }
    
    /**
     * Updates an existing agent's details.
     *
     * @param agentId The ID of the agent to update.
     * @param updateAgent The Agent object containing updated data.
     * @return The updated Agent object.
     * @throws RuntimeException if the agent is not found.
     */
 
//	@Override
//	public Agent updateAgent(Integer agentId, Agent updateAgent) {
//		logger.info("Updating agent with ID: {}",agentId);
//		return repo.findById(agentId)
//				.map(agent -> {
//					logger.debug("Existing agent data: {}");
//					agent.setName(updateAgent.getName());
//					agent.setContactInfo(updateAgent.getContactInfo());
//					agent.setAgentId(updateAgent.getAgentId());
////					agent.setPolicyId(updateAgent.getPolicyId());
////					agent.setClaimId(updateAgent.getClaimId());
////					agent.setCustomerId(updateAgent.getCustomerId());
//					
//					
//					return repo.save(agent);
//				})
//				.orElseThrow(()->{
//				logger.error("Agent not found for update with ID: {}",agentId);	
//				return new RuntimeException("Agent not found");
//		        
//				});
//	
//	}
    @Override
    public Agent updateAgent(Integer agentId, Agent updatedAgent) {
        Optional<Agent> existingAgentOptional = repo.findById(agentId);

        if (existingAgentOptional.isPresent()) {
            Agent existingAgent = existingAgentOptional.get();

            // Update only the allowed fields from the incoming payload
            existingAgent.setName(updatedAgent.getName());
            existingAgent.setContactInfo(updatedAgent.getContactInfo());
            existingAgent.setOrgEmail(updatedAgent.getOrgEmail());
            existingAgent.setGender(updatedAgent.getGender());
            existingAgent.setDate(updatedAgent.getDate());
            existingAgent.setAadharnumber(updatedAgent.getAadharnumber());
            existingAgent.setPhone(updatedAgent.getPhone());
            existingAgent.setAddress(updatedAgent.getAddress());

            // The following fields are intentionally NOT updated to protect data integrity
            // existingAgent.setAgentId(updatedAgentData.getAgentId()); // Do not update ID
            // existingAgent.setPassword(updatedAgentData.getPassword()); // Do not update password
            // existingAgent.setRole(updatedAgentData.getRole()); // Do not update role

            return repo.save(existingAgent);
        } else {
            // Handle the case where the agent is not found, e.g., by throwing an exception
            throw new RuntimeException("Agent not found with ID: " + agentId);
        }
    }
	/**
	 * Deletes an agent by their ID.
	 *
	 * @param agentId The ID of the agent to delete.
	 * @return A confirmation message indicating successful deletion.
	 */
 
	@Override
	public String deleteAgent(Integer agentId) {
		  logger.info("Deleting agent with ID:{}", agentId);
	      repo.deleteById(agentId);
	      logger.debug("Agent deleted successfully");
	      return "Agent Deleted";
	}
	
	/**
	 * Retrieves all agents associated with a specific policy ID.
	 *
	 * @param policyId The ID of the policy to filter agents by.
	 * @return A list of Agent objects linked to the given policy.
	 */
	
	
//	public List<Agent> getAgentByPolicy(Integer policyId){
//		logger.info("Fetching agents by policy ID: {}");
//		return repo.findByPolicyId(policyId);
//	}
	
 
	/**
	 * Combines agent details with their associated policies.
	 *
	 * @param aid The ID of the agent.
	 * @return An AgentPolicy object containing agent and policy data.
	 */
 
	@Override
	public AgentPolicy getAgentPolyCombo(Integer aid) {
	  logger.info("Fetching policy combination for agent ID: {}", aid);
	  List<PolicyDTO> policydto = policyclient.getPolicies(aid);
	  Optional<Agent> opt = repo.findById(aid);
	  if(opt.isEmpty()) {
		  logger.warn("Agent not found for policy combo with Id: {}",aid);
		  
	  }
	  Agent agent = opt.get();
	  AgentPolicy agentpolicy = new AgentPolicy();
	  agentpolicy.setAgent(agent);
	  agentpolicy.setPolicy(policydto);
	  
	 return agentpolicy;
	}
    
	/**
	 * Fetches customer details linked to a specific agent.
	 *
	 * @param cid The customer ID associated with the agent.
	 * @return A CustomerDTO object containing customer information.
	 */
 
 
	@Override
	public CustomerDTO getCustomerForAgent(Integer cid) {
		logger.info("Fetching customer for agent with customer ID: {}", cid);
		CustomerDTO custdto = customerclient.getCustomerForAgent(cid);
		logger.debug("Customer data retrieved: {}", custdto);
		return custdto;
	}
	/**
	 * Retrieves all claims and links them to the first available agent.
	 *
	 * @return An AgentClaim object containing agent and claim data.
	 */
 
	@Override
	public AgentClaim getAllClaims() {
		logger.info("Fetching all claims from Claimclient");
		List<ClaimDTO> claims = claimclient.getAllClaims().getBody();
		logger.debug("Total claims retrieved: {}",claims.size());
		Integer agentId = claims.isEmpty() ? null : claims.get(0).getAgentId();
	    Optional<Agent> opt = repo.findById(agentId);
	    Agent agent = opt.orElse(new Agent()); // fallback if not found
	    AgentClaim agentClaim = new AgentClaim();
	    agentClaim.setAgent(agent);
	    agentClaim.setClaim(claims);
	    return agentClaim;
	}
	/**
	 * Approves or rejects a claim based on policy threshold logic.
	 *
	 * @param claimId The ID of the claim to evaluate.
	 * @return A ClaimDTO object with updated claim status.
	 * @throws ResourceNotFoundException if the claim or policy is not found.
	 */
	
//	@Override
//    public ClaimDTO approveClaim(Integer claimId) {
//		logger.info("Approving claim with ID: {}", claimId);
//     
//		// Step 1: Get the claim
//	    ClaimDTO claim = claimclient.getClaimById(claimId).getBody();
//	    if (claim == null) {
//	        throw new ResourceNotFoundException("Claim not found with ID: " + claimId);
//	    }
// 
//	    // Step 2: Get the related policy
//	    PolicyDTO policy = policyclient.getPolicyById(claim.getPolicyId()); // You may need to add this method in PolicyClient
//	    if (policy == null) {
//	        throw new ResourceNotFoundException("Policy not found with ID: " + claim.getPolicyId());
//	    }
// 
//	    // Step 3: Calculate threshold
//	    double threshold = policy.getPremiumAmount() * policy.getValidityPeriod();
//	    logger.debug("Threshold amount: {}", threshold);
//	    logger.debug("Claim amount: {}", claim.getClaimAmount());
// 
//	    // Step 4: Compare and decide
//	    ClaimDTO.ClaimStatus status;
//	    if (threshold < claim.getClaimAmount()) {
//	        status = ClaimDTO.ClaimStatus.REJECTED;
//	        logger.info("Claim rejected due to exceeding threshold.");
//	    } else {
//	        status = ClaimDTO.ClaimStatus.APPROVED;
//	        logger.info("Claim approved.");
//	    }
// 
//	    // Step 5: Update claim status
//	    return claimclient.updateClaimStatus(claimId, status).getBody();
//    }
//	
//	@Override
//	public ClaimDTO approveClaim(Integer claimId) {
//	    logger.info("Manually approving claim with ID: {}", claimId);
//	    
//	    // Simply update claim status to APPROVED
//	    return claimclient.updateClaimStatus(claimId, ClaimDTO.ClaimStatus.APPROVED).getBody();
//	}
	@Override
	public void approveClaim(Integer claimId) {
	    logger.info("Manually approving claim with ID: {}", claimId);
	    
	    // Simply update claim status to APPROVED
	    claimclient.updateClaimStatus(claimId, ClaimDTO.ClaimStatus.APPROVED);
	}


	// Add reject method
	@Override
	public ClaimDTO rejectClaim(Integer claimId) {
	    logger.info("Manually rejecting claim with ID: {}", claimId);
	    
	    // Simply update claim status to REJECTED
	    return claimclient.updateClaimStatus(claimId, ClaimDTO.ClaimStatus.REJECTED).getBody();
	}

	
 
	/**
	 * Retrieves full details of an agent including their policies and customer info.
	 *
	 * @param agentId The ID of the agent to fetch details for.
	 * @return An AgentFullDetails object containing agent, policy, and customer data.
	 * @throws ResourceNotFoundException if the agent is not found.
	 */
	
//	public AgentFullDetails getAgentFullDetails(Integer agentId) {
//		logger.info("Fetching full details for agent ID: {}", agentId);
//		Optional<Agent> optAgent = repo.findById(agentId);
//		
//	    if (optAgent.isEmpty()) {
//	    	
//	        throw new ResourceNotFoundException("Agent not found with ID: " + agentId);
//	    }
// 
//	    Agent agent = optAgent.get();
//	    List<PolicyDTO> agentPolicies = policyclient.getPolicies(agentId);
////	    List<PolicyDTO> customerPolicies = policyclient.getPolicies(agent.getCustomerId());
//        
////	    CustomerDTO customer =customerclient.getCustomerForAgent(agent.getCustomerId());
//	    
//	    AgentFullDetails details = new AgentFullDetails();
//	    details.setAgent(agent);
//	    details.setAgentPolicies(agentPolicies);
////	    details.setCustomerPolicies(customerPolicies);
//	    details.setCustomer(customer);
// 
//	    return details;
//	}
	/**
	 * Authenticates an agent using contact info and password.
	 *
	 * @param contactInfo The contact information used for login.
	 * @param password The password provided by the agent.
	 * @return A welcome message if credentials are valid.
	 * @throws InvalidCredentialsException if authentication fails.
	 */
 
	@Override
	public String loginAgent(String contactInfo, String password) {
		Agent agent = repo.findByOrgEmail(contactInfo);
		
        if (agent == null || !agent.getPassword().equals(password)) {
            throw new InvalidCredentialsException("Invalid email or password.");
        }
 
        return "Welcome Home, " + agent.getName() + "!";
	}
	
	/**
	 * Retrieves all agents linked to a specific policy ID.
	 *
	 * @param policyId The ID of the policy to search agents by.
	 * @return A list of Agent objects associated with the given policy.
	 */
 
//	@Override
//	public List<Agent> getallagentsbypolicyId(Integer policyId) {
//		
//		return repo.findAllByPolicyId(policyId);
//	}

	@Override
	public Agent getAgentByEmail(String email) {
		return repo.findByOrgEmail(email);
	}
	
		
}
 