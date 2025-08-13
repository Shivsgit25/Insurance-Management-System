package com.project.service;

import com.project.model.Customer;
import com.project.model.CustomerPolicy;

public interface CustomerService {

	public String AddCustomer(Customer cust);

	public String UpdateCustomer(Customer customer);

	public Customer getCustomerById(int id);

	public String deleteByCustomerId(int id);

	public CustomerPolicy getCustPolyCombo(Integer cid);
}
