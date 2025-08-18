package com.project.service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;

import com.project.model.Agent;
import com.project.model.AgentClaim;
import com.project.model.AgentCustomer;
import com.project.model.AgentFullDetails;
import com.project.model.AgentPolicy;
import com.project.model.ClaimDTO;
import com.project.model.CustomerDTO;

public interface AgentService {
	
	String createAgent(Agent agent);
	
	

//	
//	String deleteAgent(Long agentId);

	List<Agent> getAllAgents();
	
	Optional<Agent> getAgentById(Integer agentId);
	
	Agent updateAgent(Integer agentId, Agent updateAgent);
	
	
	String deleteAgent(Integer agentId);


     List<Agent> getAgentByPolicy(Integer policyId);



	AgentPolicy getAgentPolyCombo(Integer customerId);



//	AgentCustomer getAgentCustCombo(Integer aid);



	CustomerDTO getCustomerForAgent(Integer aid);



	AgentClaim getAllClaims();



	ClaimDTO approveClaim(Integer claimId);



	AgentFullDetails getAgentFullDetails(Integer agentId);



//	Object getAllClaims();



	

}
