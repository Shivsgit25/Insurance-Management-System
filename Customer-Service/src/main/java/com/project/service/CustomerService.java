package com.project.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.project.model.ClaimDTO;
import com.project.model.Customer;
import com.project.model.CustomerPolicy;

public interface CustomerService {

	public String AddCustomer(Customer cust);

	public String UpdateCustomer(Customer customer);

	public Customer getCustomerById(int id);

	public String deleteByCustomerId(int id);

	public CustomerPolicy getCustPolyCombo(Integer cid);

	public List<Customer> getAllCustomer();

	public List<Customer> getCustomerInfoForClaim(Integer cid);

	public void fileClaim(ClaimDTO claim);

	public Customer getCustomerForAgent(Integer id);
}
