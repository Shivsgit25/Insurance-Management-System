package com.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import com.project.DTO.AgentDTO;
import com.project.DTO.CustomerDTO;
import com.project.model.AuthRequest;
import com.project.service.AuthService;
import reactor.core.publisher.Mono;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<Mono<String>> login(@RequestBody AuthRequest request) {
        Mono<String> token = authService.authenticate(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(token);
    }

    @PostMapping("/register")
    public Mono<ResponseEntity<String>> registerNewUser(@RequestBody CustomerDTO user) {
        // We now handle the error as part of the reactive chain
        return authService.registerUser(user)
                .map(responseBody -> ResponseEntity.ok(responseBody)) // On success, return a 200 OK
                .onErrorResume(WebClientResponseException.Conflict.class, ex -> {
                    // On a 409 Conflict, extract the error message from the response body
                    String errorMessage = ex.getResponseBodyAsString();
                    return Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage));
                })
                .onErrorResume(throwable -> {
                    // Handle other exceptions (e.g., connection issues)
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body("An unexpected error occurred: " + throwable.getMessage()));
                });
    }

    @PostMapping("/agentlogin")
    public ResponseEntity<Mono<String>> agentlogin(@RequestBody AuthRequest request) {
        Mono<String> token = authService.authenticateAgent(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(token);
    }
    
    @PostMapping("/registeragent")
    public Mono<ResponseEntity<String>> registerAgent(@RequestBody AgentDTO agent){
        return authService.registerAgent(agent)
                .map(responseBody -> ResponseEntity.ok(responseBody))
                .onErrorResume(WebClientResponseException.Conflict.class, ex -> {
                    String errorMessage = ex.getResponseBodyAsString();
                    return Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage));
                })
                .onErrorResume(throwable -> {
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body("An unexpected error occurred during agent registration: " + throwable.getMessage()));
                });
    }
}