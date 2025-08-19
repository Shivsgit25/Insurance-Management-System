package com.project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

import com.project.utility.JwtAuthenticationFilter;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

	private final JwtAuthenticationFilter jwtAuthenticationFilter;

	public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
		this.jwtAuthenticationFilter = jwtAuthenticationFilter;
	}

	@Bean
	public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
		return http.csrf(ServerHttpSecurity.CsrfSpec::disable)
				.authorizeExchange(exchanges -> exchanges.pathMatchers("/auth/login", "/auth/register").permitAll()
						.pathMatchers(HttpMethod.GET,"/**").hasRole("ADMIN")
						.pathMatchers(HttpMethod.PUT,"/**").hasRole("ADMIN")
						.pathMatchers(HttpMethod.POST,"/**").hasRole("ADMIN")
						.pathMatchers(HttpMethod.DELETE,"/**").hasRole("ADMIN")
						.pathMatchers(HttpMethod.POST,"/api/policies/create","/api/claims/file").hasRole("CUSTOMER")
						.pathMatchers(HttpMethod.GET,"/api/claims/file","/api/policies","/api/claims/").hasRole("AGENT")
						.pathMatchers(HttpMethod.PUT,"/agents/approve-claim").hasRole("AGENT")
						.anyExchange().authenticated())
				.addFilterAt(jwtAuthenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION).build();
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
