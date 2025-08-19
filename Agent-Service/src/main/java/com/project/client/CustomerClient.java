package com.project.client;



import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.project.model.CustomerDTO;

@FeignClient(name="CUSTOMER-SERVICE", path="/customer")
public interface CustomerClient {
	
	
	
	@GetMapping("/getCustomerForAgent/{id}")
	public CustomerDTO getCustomerForAgent(@PathVariable("id") Integer id);
	
	
}
