package com.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.DTO.CustomerDTO;
import com.project.model.AuthRequest;
import com.project.service.AuthService;

import reactor.core.publisher.Mono;

@RestController
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
    public ResponseEntity<Mono<String>> registerNewUser(@RequestBody CustomerDTO user) {
        // Correctly return the Mono from the service layer.
        // Spring WebFlux will subscribe to this Mono automatically,
        // triggering the WebClient call.
        return ResponseEntity.ok(authService.registerUser(user));
    }
}