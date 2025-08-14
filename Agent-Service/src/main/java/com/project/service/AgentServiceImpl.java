package com.project.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.client.PolicyClient;
import com.project.model.Agent;
import com.project.model.AgentPolicy;
import com.project.model.PolicyDTO;
import com.project.repository.AgentRepository;
//import com.project.repository.AgentRepository;


@Service
public class AgentServiceImpl implements AgentService {
	
	@Autowired
	private AgentRepository repo;
	
	@Autowired
	PolicyClient policyclient;

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

	
	public List<Agent> getAgentByCustomer(Integer customerId) {
		
		return repo.findByCustomerId(customerId);
	}

	@Override
	public AgentPolicy getAgentPolyCombo(Integer aid) {
	  List<PolicyDTO> policydto = policyclient.getCollection(aid);
	  Optional<Agent> opt = repo.findById(aid);
	  Agent agent = opt.get();
	  AgentPolicy agentpolicy = new AgentPolicy();
	  agentpolicy.setAgent(agent);
	  agentpolicy.setPolicy(policydto);
	  
	 return agentpolicy;
	}



	

	
	


}
