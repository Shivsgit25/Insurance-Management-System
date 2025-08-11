package com.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.model.Customer;
import com.project.service.CustomerService;

@RestController
@RequestMapping("/customer")
public class CustomerController {
	
	@Autowired
	CustomerService service;
	
	@PostMapping("/add")
	public String AddCustomer(@RequestBody Customer customer) {
		return service.AddCustomer(customer);
	}
	
	@PostMapping("/Update")
	public String UpdateCustomer(@RequestBody Customer customer) {
		return service.UpdateCustomer(customer);
	}
	@GetMapping("/getCustomer/{id}")
	public Customer getCustomerById(@PathVariable("id") int id) {
		return service.getCustomerById(id);
	}
	@PostMapping("/deleteCustomer/{id}")
	public String deleteCustomerById(@PathVariable("id") int id) {
		return service.deleteByCustomerId(id);
	}
}
