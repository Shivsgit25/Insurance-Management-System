package com.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.model.Customer;

public interface CustomerRepository  extends JpaRepository<Customer, Integer>{

	Customer findByCustomerId(int id);

	Customer findByEmail(String email);
	

	Customer findByEmail(String email); 

}
