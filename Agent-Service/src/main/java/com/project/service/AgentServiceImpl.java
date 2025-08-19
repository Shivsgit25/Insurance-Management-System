package com.project.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.client.ClaimClient;
import com.project.client.CustomerClient;
import com.project.client.PolicyClient;
import com.project.exception.InvalidCredentialsException;
import com.project.exception.ResourceNotFoundException;
import com.project.model.Agent;
import com.project.model.AgentClaim;
import com.project.model.AgentFullDetails;
import com.project.model.AgentPolicy;
import com.project.model.ClaimDTO;
import com.project.model.CustomerDTO;
import com.project.model.PolicyDTO;
import com.project.repository.AgentRepository;



@Service
public class AgentServiceImpl implements AgentService {
	
	@Autowired
	private AgentRepository repo;
	
	@Autowired
	PolicyClient policyclient;
	
	@Autowired
	CustomerClient customerclient;
	
	@Autowired
	ClaimClient claimclient;
	
	private static final Logger logger = LoggerFactory.getLogger(AgentServiceImpl.class);

	@Override
	public String createAgent(Agent agent) {
		logger.info("Creating agent with ID: {}", agent.getAgentId());
	    repo.save(agent);
	    logger.debug("Agent Saved: {}",agent);
	    return "Agent saved";
	    
	}

	@Override
	public List<Agent> getAllAgents() {
		logger.info("Fetching all agents");
		
		return repo.findAll();
		
	
	}
	
    @Override
    public Optional<Agent> getAgentById(Integer agentId){
    	logger.info("Fetching agent by ID: {}",agentId);
    	return repo.findById(agentId);
    	
    }

	@Override
	public Agent updateAgent(Integer agentId, Agent updateAgent) {
		logger.info("Updating agent with ID: {}",agentId);
		return repo.findById(agentId)
				.map(agent -> {
					logger.debug("Existing agent data: {}");
					agent.setName(updateAgent.getName());
					agent.setContactInfo(updateAgent.getContactInfo());
					agent.setAgentId(updateAgent.getAgentId());
					agent.setPolicyId(updateAgent.getPolicyId());
					agent.setClaimId(updateAgent.getClaimId());
					agent.setCustomerId(updateAgent.getCustomerId());
					
					
					return repo.save(agent);
				})
				.orElseThrow(()->{
				logger.error("Agent not found for update with ID: {}",agentId);	
				return new RuntimeException("Agent not found");
		        
				});
	
	}

	@Override
	public String deleteAgent(Integer agentId) {
		  logger.info("Deleting agent with ID:{}", agentId);
	      repo.deleteById(agentId);
	      logger.debug("Agent deleted successfully");
	      return "Agent Deleted";
	}
	
	
	
	
	public List<Agent> getAgentByPolicy(Integer policyId){
		logger.info("Fetching agents by policy ID: {}");
		return repo.findByPolicyId(policyId);
	}

	

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



	@Override
	public CustomerDTO getCustomerForAgent(Integer cid) {
		logger.info("Fetching customer for agent with customer ID: {}", cid);
		CustomerDTO custdto = customerclient.getCustomerForAgent(cid);
		logger.debug("Customer data retrieved: {}", custdto);
		return custdto;
	}

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
	
	@Override
    public ClaimDTO approveClaim(Integer claimId) {
		logger.info("Approving claim with ID: {}", claimId);
     
		// Step 1: Get the claim
	    ClaimDTO claim = claimclient.getClaimById(claimId).getBody(); 
	    if (claim == null) {
	        throw new ResourceNotFoundException("Claim not found with ID: " + claimId);
	    }

	    // Step 2: Get the related policy
	    PolicyDTO policy = policyclient.getPolicyById(claim.getPolicyId()); // You may need to add this method in PolicyClient
	    if (policy == null) {
	        throw new ResourceNotFoundException("Policy not found with ID: " + claim.getPolicyId());
	    }

	    // Step 3: Calculate threshold
	    double threshold = policy.getPremiumAmount() * policy.getValidityPeriod();
	    logger.debug("Threshold amount: {}", threshold);
	    logger.debug("Claim amount: {}", claim.getClaimAmount());

	    // Step 4: Compare and decide
	    ClaimDTO.ClaimStatus status;
	    if (threshold < claim.getClaimAmount()) {
	        status = ClaimDTO.ClaimStatus.REJECTED;
	        logger.info("Claim rejected due to exceeding threshold.");
	    } else {
	        status = ClaimDTO.ClaimStatus.APPROVED;
	        logger.info("Claim approved.");
	    }

	    // Step 5: Update claim status
	    return claimclient.updateClaimStatus(claimId, status).getBody();
    }


	
	public AgentFullDetails getAgentFullDetails(Integer agentId) {
		logger.info("Fetching full details for agent ID: {}", agentId);
		Optional<Agent> optAgent = repo.findById(agentId);
		
	    if (optAgent.isEmpty()) {
	    	
	        throw new ResourceNotFoundException("Agent not found with ID: " + agentId);
	    }

	    Agent agent = optAgent.get();
	    List<PolicyDTO> agentPolicies = policyclient.getPolicies(agentId);
	    List<PolicyDTO> customerPolicies = policyclient.getPolicies(agent.getCustomerId());

	    AgentFullDetails details = new AgentFullDetails();
	    details.setAgent(agent);
	    details.setAgentPolicies(agentPolicies);
	    details.setCustomerPolicies(customerPolicies);

	    return details;
	}

	@Override
	public String loginAgent(String contactInfo, String password) {
		Agent agent = repo.findByContactInfo(contactInfo);
		 
        if (agent == null || !agent.getPassword().equals(password)) {
            throw new InvalidCredentialsException("Invalid email or password.");
        }
 
        return "Welcome Home, " + agent.getName() + "!";
	}



	

	
	


}
