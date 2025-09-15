package com.project.config;
 
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
 
import com.project.utility.JwtAuthenticationFilter;
 
import java.util.Arrays;
 
@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
 
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
 
    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }
 
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
 
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http.csrf(ServerHttpSecurity.CsrfSpec::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeExchange(exchanges -> exchanges
                    // Public endpoints
                    .pathMatchers("/auth/login", "/auth/register","/auth/agentlogin","/api/policylist").permitAll()
                    
                 // CUSTOMER role endpoints
                    .pathMatchers(HttpMethod.GET, "/api/policies").hasAnyRole("CUSTOMER", "ADMIN")
                    .pathMatchers(HttpMethod.GET, "/api/policies/getCustomerPolicyDetails/**","api/claims/customer/**").hasRole("CUSTOMER")
                    .pathMatchers(HttpMethod.GET, "/api/policies/**").hasAnyRole("CUSTOMER","AGENT")
                    .pathMatchers(HttpMethod.GET, "/customer/getCustomer/**").hasAnyRole("CUSTOMER","AGENT")
                    .pathMatchers(HttpMethod.PUT, "/customer/Update").hasRole("CUSTOMER")
                    .pathMatchers(HttpMethod.POST, "/api/claims/file","api/policies/create").hasRole("CUSTOMER")
                    .pathMatchers(HttpMethod.GET, "/api/claims/customer/**").hasRole("CUSTOMER")
                    
                    // AGENT role endpoints
                    .pathMatchers(HttpMethod.GET, "/api/claims/file").hasRole("AGENT")
                    .pathMatchers(HttpMethod.PUT, "/agents/approve-claim").hasRole("AGENT")
                    .pathMatchers(HttpMethod.GET,"/agents/claims/all","/agents/getCustomerForAgent/**","/agents/getAgentPolicyDetails/**").hasRole("AGENT")
                    .pathMatchers(HttpMethod.PUT,"/agents/approve-claim/**","/agents/reject-claim/**").hasRole("AGENT")
                    
                    // ADMIN role endpoints
                    .pathMatchers(HttpMethod.POST, "/auth/registeragent").hasRole("ADMIN")
                    .pathMatchers(HttpMethod.GET, "/agents/all").hasRole("ADMIN")
                    .pathMatchers(HttpMethod.DELETE, "/agents/delete/**").hasRole("ADMIN")
                    .pathMatchers(HttpMethod.GET, "/api/claims/claims/all").hasRole("ADMIN")
                    .pathMatchers(HttpMethod.GET, "/customer/getAllCustomer").hasRole("ADMIN")
                    .pathMatchers(HttpMethod.GET, "/api/policylist").hasRole("ADMIN")
                    .pathMatchers(HttpMethod.DELETE, "/api/policylist/**").hasRole("ADMIN")
                    .pathMatchers(HttpMethod.POST, "/api/policylist").hasRole("ADMIN")
                    .pathMatchers(HttpMethod.POST, "/api/mail/send").hasRole("ADMIN")
                    .pathMatchers(HttpMethod.POST, "/api/sms/send").hasRole("ADMIN")
                    
                    .anyExchange().authenticated())
                .addFilterAt(jwtAuthenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }
 
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}