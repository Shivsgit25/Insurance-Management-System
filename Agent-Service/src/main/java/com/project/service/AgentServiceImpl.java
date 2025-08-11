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
	

    public Optional<Agent> getAgentById(int agentId){
    	return repo.findById(agentId);
    	
    }
	
	

//	@Override
//	public Optional<Agent> getAgentById(Long agentId) {
//
//		return repo.findById(agentId);
//	}

//	@Override
//	public Agent updateAgent(Long id, Agent updatedAgent) {
//	
//		return repo.findById(id)
//				.map(agent -> {
//					agent.setName(updatedAgent.getName());
//					agent.setContactInfo(updatedAgent.getContactInfo());
//					agent.setAssignedPolicies(updatedAgent.getAssignedPolicies());
//					return repo.save(agent);
//					
//				})
//				.orElseThrow(() -> new RuntimeException("Agent not found"));
//	}
//
//	@Override
//	public String deleteAgent(int agentId) {
//		repo.deleteById(agentId);
//		return "Agent deleted";
//		
//	}

}
