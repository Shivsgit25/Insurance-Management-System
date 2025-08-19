package com.project.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.project.model.AgentDTO;

@FeignClient(name = "AGENTSERVICE", path = "/agents")
public interface AgentClient {
	 @GetMapping("/getAgentDetails/{policyId}")
	  public List<AgentDTO> getAgents(@PathVariable("policyId") Integer policyId);
}
