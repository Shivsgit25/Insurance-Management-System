package com.project.service;

import java.util.List;
import java.util.Optional;

import com.project.model.Agent;
import com.project.model.AgentClaim;
import com.project.model.AgentPolicy;
import com.project.model.ClaimDTO;
import com.project.model.CustomerDTO;

public interface AgentService {
	
	String createAgent(Agent agent);
	
	



	List<Agent> getAllAgents();
	
	Optional<Agent> getAgentById(Integer agentId);
	
//	Agent updateAgent(Integer agentId, Agent updateAgent);
	
	
	String deleteAgent(Integer agentId);


//     List<Agent> getAgentByPolicy(Integer policyId);



	AgentPolicy getAgentPolyCombo(Integer customerId);







	CustomerDTO getCustomerForAgent(Integer aid);



	AgentClaim getAllClaims();



	ClaimDTO approveClaim(Integer claimId);



//	AgentFullDetails getAgentFullDetails(Integer agentId);





	String loginAgent(String contactInfo, String password);





//	List<Agent> getallagentsbypolicyId(Integer policyId);





	Agent getAgentByEmail(String email);







	

}
