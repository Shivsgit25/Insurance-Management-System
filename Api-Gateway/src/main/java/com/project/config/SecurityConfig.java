package com.project.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import static org.springframework.security.config.Customizer.withDefaults;
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
				.cors(withDefaults())
				.authorizeExchange(exchanges -> exchanges.pathMatchers("/auth/login", "/auth/register").permitAll()
						.pathMatchers(HttpMethod.POST,"/api/policies/create","/api/claims/file","/auth/login").hasRole("CUSTOMER")
						.pathMatchers(HttpMethod.GET,"/api/policies/**","/api/policies","api/policies/customer/**","customer/getCustomer/**").hasRole("CUSTOMER")
						.pathMatchers(HttpMethod.GET,"/api/claims/file","/api/policies","/api/claims/").hasRole("AGENT")
						.pathMatchers(HttpMethod.PUT,"/customer/Update/**").hasRole("CUSTOMER")
						.pathMatchers(HttpMethod.PUT,"/agents/approve-claim").hasRole("AGENT")
						.pathMatchers(HttpMethod.GET,"/**").hasRole("ADMIN")
						.pathMatchers(HttpMethod.PUT,"/**").hasRole("ADMIN")
						.pathMatchers(HttpMethod.POST,"/**").hasRole("ADMIN")
						.pathMatchers(HttpMethod.DELETE,"/**").hasRole("ADMIN")
						.anyExchange().authenticated())
				.addFilterAt(jwtAuthenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION).build();
	}

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
 
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
