package com.project.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.model.Agent;
import com.project.repository.AgentRepository;
//import com.project.repository.AgentRepository;


@Service
public class AgentServiceImpl implements AgentService {
	
	@Autowired
	private AgentRepository repo;

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

	

	
	


}
