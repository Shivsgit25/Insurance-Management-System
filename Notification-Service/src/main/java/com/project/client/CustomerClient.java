package com.project.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.project.DTO.CustomerDTO;

@FeignClient(name = "CUSTOMER-SERVICE", path="/customer")

public interface CustomerClient {
	
	@GetMapping("/getCustomer/{id}")
	public CustomerDTO getCustomerById(@PathVariable int id);

}
