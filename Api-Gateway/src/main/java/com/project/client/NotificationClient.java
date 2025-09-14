package com.project.client;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.project.DTO.loginCredentialsDto;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class NotificationClient {
	@LoadBalanced
	private final WebClient.Builder webClientBuilder;

	public NotificationClient(WebClient.Builder webClientBuilder) {
		this.webClientBuilder = webClientBuilder;
	}

	public Mono<String> sendAgentCred(loginCredentialsDto agent) {
	    log.info("Attempting to call Notification Service...");
	    return webClientBuilder.build()
	            .post()
	            .uri("http://NOTIFICATION-SERVICE/notify/sendAgentCred")
	            .bodyValue(agent)
	            .retrieve()
	            .bodyToMono(String.class)
	            // Add this line to handle and log any errors
	            .doOnError(throwable -> log.error("Error making call to Notification Service", throwable));
	}

}