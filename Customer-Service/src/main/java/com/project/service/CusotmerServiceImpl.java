package com.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.model.Customer;
import com.project.repository.CustomerRepository;

@Service
public class CusotmerServiceImpl implements CustomerService {

	@Autowired
	CustomerRepository repo;
	
	
	@Override
	public String AddCustomer(Customer customer) {
		repo.save(customer);
		return "Customer Saved Successfully";
		
	}

	@Override
	public String UpdateCustomer(Customer customer) {
		repo.save(customer);
		int id = customer.getCustomerId();
		return "Customer Updated Successfully on ID : "+id;
	}

	@Override
	public Customer getCustomerById(int id) {
		Customer customer=repo.findByCustomerId(id);
		return customer;
	}

	@Override
	public String deleteByCustomerId(int id) {
		Customer customer = repo.findByCustomerId(id);
		int customerid=customer.getCustomerId();
		String customername=customer.getName();
		repo.delete(customer);
		return "The Data for the Customer id : "+customerid +"\nCustomer Name :"+customername+"\n Deleted Successfully !!!";
	}
	
	
	

}
