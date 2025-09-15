package com.project.clients;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import com.project.DTO.AgentDTO;


@FeignClient(name = "AGENTSERVICE", path = "/agents")
public interface AgentClient {
	
	@GetMapping("/all")
	public ResponseEntity<List<AgentDTO>> getAllAgents();
	
}
