package com.project.clients;

import org.aspectj.weaver.loadtime.Agent;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "AGENTSERVICE", path = "/agent")
public interface AgentClient {
    @GetMapping("/agent/{id}")
    Agent getAgentById(@PathVariable("agent_id") Integer agentId);
}
