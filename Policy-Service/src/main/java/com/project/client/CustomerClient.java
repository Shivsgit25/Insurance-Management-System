package com.project.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.project.model.CustomerDTO;

@FeignClient(name = "CUSTOMER-SERVICE", path = "/customer")
public interface CustomerClient {

	@GetMapping("/getCustomerDetails/{policyId}")
	List<CustomerDTO> getCustomers(@PathVariable("policyId") Integer policyId);

	@GetMapping("/getCustomerNames/{policyId}")
	public ResponseEntity<List<String>> getCustomerNames(@PathVariable Integer policyId);

}
