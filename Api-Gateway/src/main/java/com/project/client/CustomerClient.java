package com.project.client;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;

//import org.springframework.cloud.openfeign.FeignClient;

//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//
//import com.project.DTO.CustomerDTO;
//
//
//
//@FeignClient(name = "CUSTOMER-SERVICE", path="/customer")
//
//public interface CustomerClient {
//	
//	@PostMapping("/add")
//    public String AddCustomer(@RequestBody CustomerDTO customer);
//	
//	@GetMapping("/getCustomer/{id}")
//	public CustomerDTO getCustomerById(@PathVariable int id);
//	
//    @GetMapping("/getCustomerByEmail/{email}")
//    public CustomerDTO getCustomerByEmail(@PathVariable("email") String email); 
//
//}

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.project.DTO.CustomerDTO;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class CustomerClient {
	@LoadBalanced
	private final WebClient.Builder webClientBuilder;

	public CustomerClient(WebClient.Builder webClientBuilder) {
		this.webClientBuilder = webClientBuilder;
	}

	public Mono<String> addCustomer(CustomerDTO customer) {
	    log.info("Attempting to call CUSTOMER-SERVICE...");
	    return webClientBuilder.build()
	            .post()
	            .uri("http://CUSTOMER-SERVICE/customer/add")
	            .bodyValue(customer)
	            .retrieve()
	            .bodyToMono(String.class)
	            // Add this line to handle and log any errors
	            .doOnError(throwable -> log.error("Error making call to CUSTOMER-SERVICE", throwable));
	}

	public Mono<CustomerDTO> getCustomerByEmail(String email) {
		return webClientBuilder.build().get().uri("http://CUSTOMER-SERVICE/customer/getCustomerByEmail/{email}", email)
				.retrieve().bodyToMono(CustomerDTO.class);
	}
}