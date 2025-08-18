package com.project.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.client.ClaimClient;
import com.project.client.CustomerClient;
import com.project.client.PolicyClient;
import com.project.exception.ResourceNotFoundException;
import com.project.model.Agent;
import com.project.model.AgentClaim;
import com.project.model.AgentCustomer;
import com.project.model.AgentFullDetails;
import com.project.model.AgentPolicy;
import com.project.model.ClaimDTO;
import com.project.model.CustomerDTO;
import com.project.model.PolicyDTO;
import com.project.repository.AgentRepository;
//import com.project.repository.AgentRepository;


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

	@Override
	public String createAgent(Agent agent) {
	    repo.save(agent);
	    return "Agent saved";
	    
	}

	@Override
	public List<Agent> getAllAgents() {
		return repo.findAll();
	
	}
	
    @Override
    public Optional<Agent> getAgentById(Integer agentId){
    	return repo.findById(agentId);
    	
    }

	@Override
	public Agent updateAgent(Integer agentId, Agent updateAgent) {
		return repo.findById(agentId)
				.map(agent -> {
					agent.setName(updateAgent.getName());
					agent.setContactInfo(updateAgent.getContactInfo());
					agent.setAgentId(updateAgent.getAgentId());
					agent.setPolicyId(updateAgent.getPolicyId());
					agent.setClaimId(updateAgent.getClaimId());
					agent.setCustomerId(updateAgent.getCustomerId());
					
					
					return repo.save(agent);
				})
				.orElseThrow(()-> new RuntimeException("Agent not found"));
	
	}

	@Override
	public String deleteAgent(Integer agentId) {
		
	      repo.deleteById(agentId);
	      return "Agent Deleted";
	}
	
	
	
	
	public List<Agent> getAgentByPolicy(Integer policyId){
		return repo.findByPolicyId(policyId);
	}

	

	@Override
	public AgentPolicy getAgentPolyCombo(Integer aid) {
	  List<PolicyDTO> policydto = policyclient.getPolicies(aid);
	  Optional<Agent> opt = repo.findById(aid);
	  Agent agent = opt.get();
	  AgentPolicy agentpolicy = new AgentPolicy();
	  agentpolicy.setAgent(agent);
	  agentpolicy.setPolicy(policydto);
	  
	 return agentpolicy;
	}

//	@Override
//	public AgentCustomer getAgentCustCombo(Integer aid) {
//		// TODO Auto-generated method stub
//		return null;
//	}

	@Override
	public CustomerDTO getCustomerForAgent(Integer cid) {
	
		CustomerDTO custdto = customerclient.getCustomerForAgent(cid);
		
		return custdto;
	}

	@Override
	public AgentClaim getAllClaims() {
		List<ClaimDTO> claims = claimclient.getAllClaims().getBody();
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
        return claimclient.updateClaimStatus(claimId, ClaimDTO.ClaimStatus.APPROVED).getBody();
    }

//	@Override
//	public AgentCustomer getAgentCustCombo(Integer aid) {
//		
//		List<CustomerDTO> custdto = customerclient.getCustomers(aid);
//		Optional<Agent> opt = repo.findByCustomerId(aid);
//		Agent agent = opt.get();
//		AgentCustomer agentcustomer = new AgentCustomer();
//		agentcustomer.setAgent(agent);
//		agentcustomer.setCust(custdto);
//		
//		return agentcustomer;
//	}
	
	public AgentFullDetails getAgentFullDetails(Integer agentId) {
		
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



	

	
	


}
