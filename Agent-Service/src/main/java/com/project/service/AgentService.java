package com.project.service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;

import com.project.model.Agent;

public interface AgentService {
	
	String createAgent(Agent agent);
	
	

//	
//	String deleteAgent(Long agentId);

	List<Agent> getAllAgents();
	
	Optional<Agent> getAgentById(int agentId);
	
	Agent updateAgent(int agentId, Agent updateAgent);
	
	
	String deleteAgent(int agentId);

}
