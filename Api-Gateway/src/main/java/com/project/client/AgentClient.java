package com.project.client;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.project.DTO.AgentDTO;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class AgentClient {
	@LoadBalanced
	private final WebClient.Builder webClientBuilder;

	public AgentClient(WebClient.Builder webClientBuilder) {
		this.webClientBuilder = webClientBuilder;
	}

	public Mono<String> createAgent(AgentDTO agent) {
	    log.info("Attempting to call Agent Service...");
	    return webClientBuilder.build()
	            .post()
	            .uri("http://AGENTSERVICE/agents/save")
	            .bodyValue(agent)
	            .retrieve()
	            .bodyToMono(String.class)
	            // Add this line to handle and log any errors
	            .doOnError(throwable -> log.error("Error making call to Agent Service", throwable));
	}

	public Mono<AgentDTO> getAgentByEmail(String email) {
		return webClientBuilder.build().get().uri("http://AGENTSERVICE/agents/getAgentByEmail/{email}", email)
				.retrieve().bodyToMono(AgentDTO.class);
	}
}