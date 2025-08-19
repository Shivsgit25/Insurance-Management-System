package com.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.DTO.CustomerDTO;
import com.project.client.CustomerClient;
import com.project.utility.JwtUtil;
import reactor.core.publisher.Mono; // Make sure this is imported

@Service
public class AuthService {

	@Autowired
	private CustomerClient customerClient; // Correctly autowired WebClient-based client
	
	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	public Mono<String> authenticate(String username, String rawPassword) {
		return customerClient.getCustomerByEmail(username)
				.flatMap(user -> { // Use flatMap to unwrap the user from Mono<CustomerDTO>
					if (user == null) {
                        return Mono.error(new BadCredentialsException("User Not Found"));
                    }
                    if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
						return Mono.error(new BadCredentialsException("Invalid password"));
					}
					return Mono.just(jwtUtil.generateToken(user.getEmail(), user.getRole()));
				})
				.switchIfEmpty(Mono.error(new BadCredentialsException("User Not Found"))); // Handles if getCustomerByEmail returns empty
	}

	public Mono<String> registerUser(CustomerDTO customer) {
		String hashedPassword = passwordEncoder.encode(customer.getPassword());
		customer.setPassword(hashedPassword);
		// Correctly return the Mono from the addCustomer call.
		// The calling Controller (or other service) will then subscribe to this Mono.
		return customerClient.addCustomer(customer);
	}

}