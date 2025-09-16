package com.project.service;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.DTO.AgentDTO;
import com.project.DTO.CustomerDTO;
import com.project.DTO.loginCredentialsDto;
import com.project.client.AgentClient;
import com.project.client.CustomerClient;
import com.project.client.NotificationClient;
import com.project.utility.JwtUtil;

import reactor.core.publisher.Mono; // Make sure this is imported

@Service
public class AuthService {

    private final NotificationClient notificationClient;

	private final CustomerClient customerClient; // Correctly autowired WebClient-based client
	private final AgentClient agentClient;
	private final JwtUtil jwtUtil;
	private final BCryptPasswordEncoder passwordEncoder;
	AuthService(NotificationClient notificationClient , CustomerClient customerClient , AgentClient agentClient , JwtUtil jwtUtil, BCryptPasswordEncoder passwordEncoder){
		this.agentClient = agentClient;
		this.customerClient = customerClient;
		this.jwtUtil = jwtUtil;
		this.passwordEncoder = passwordEncoder;
		this.notificationClient = notificationClient;
	}

	public Mono<String> authenticate(String username, String rawPassword) {
		return customerClient.getCustomerByEmail(username)
				.flatMap(user -> { // Use flatMap to unwrap the user from Mono<CustomerDTO>
					if (user == null) {
                        return Mono.error(new BadCredentialsException("User Not Found"));
                    }
                    if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
						return Mono.error(new BadCredentialsException("Invalid password"));
					}
					return Mono.just(jwtUtil.generateToken(user.getEmail(), user.getRole() , user.getCustomerId()));
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

	
	public Mono<String> registerAgent(AgentDTO agent) {
		loginCredentialsDto data = new loginCredentialsDto();
		data.setContactInfo(agent.getContactInfo());
		data.setName(agent.getName());
		data.setOrgEmail(agent.getOrgEmail());
		data.setPassword(agent.getPassword());
		String hashedPassword = passwordEncoder.encode(agent.getPassword());
		agent.setPassword(hashedPassword);
		// Correctly return the Mono from the addCustomer call.
		// The calling Controller (or other service) will then subscribe to this Mono.
		
		Mono<String> res = agentClient.createAgent(agent);
//		Mono<String> notify = notificationClient.sendAgentCred(data);
//		Mono<String> result = Mono.zip(res ,  notify , (r1,r2)->(r1+" : "+r2));
		return res;
	}

	public Mono<String> authenticateAgent(String email, String password) {
		return agentClient.getAgentByEmail(email)
				.flatMap(user -> { // Use flatMap to unwrap the user from Mono<CustomerDTO>
					if (user == null) {
                        return Mono.error(new BadCredentialsException("User Not Found"));
                    }
                    if (!passwordEncoder.matches(password, user.getPassword())) {
						return Mono.error(new BadCredentialsException("Invalid password"));
					}
					return Mono.just(jwtUtil.generateToken(user.getContactInfo(), user.getRole(), user.getAgentId()));
				})
				.switchIfEmpty(Mono.error(new BadCredentialsException("User Not Found"))); // Handles if getCustomerByEmail returns empty
	}

}