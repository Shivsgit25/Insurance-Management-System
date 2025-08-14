package com.project.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.project.model.CustomerDTO;

@FeignClient(name = "CUSTOMER-SERVICE", path = "/customer")
public interface CustomerClient {
    @GetMapping("/customer/{id}")
    CustomerDTO getCustomerById(@PathVariable("customer_id") Integer customerId);
}

