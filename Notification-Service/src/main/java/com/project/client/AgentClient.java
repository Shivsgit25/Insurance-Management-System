package com.project.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.project.DTO.AgentDTO;

@FeignClient(name = "AGENTSERVICE", path = "/agent")
public interface AgentClient {
	
	@GetMapping("get/{agentId}")
	public ResponseEntity<AgentDTO> getAgentById(@PathVariable Integer agentId);

}
