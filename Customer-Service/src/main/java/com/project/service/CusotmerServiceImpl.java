package com.project.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.project.client.ClaimClient;
import com.project.client.PolicyClient;
import com.project.model.ClaimDTO;
import com.project.model.Customer;
import com.project.model.CustomerPolicy;
import com.project.model.PolicyDTO;
import com.project.repository.CustomerRepository;

@Service
public class CusotmerServiceImpl implements CustomerService {

	@Autowired
	CustomerRepository repo;

	@Autowired
	PolicyClient policyclient;
	@Autowired
	ClaimClient claimclient;

	@Override
	public String AddCustomer(Customer customer) {
		repo.save(customer);
		return "Customer Saved Successfully";

	}

	@Override
	public String UpdateCustomer(Customer customer) {
		repo.save(customer);
		int id = customer.getCustomerId();
		return "Customer Updated Successfully on ID : " + id;
	}

	@Override
	public Customer getCustomerById(int id) {
		Customer customer = repo.findByCustomerId(id);
		return customer;
	}

	@Override
	public List<Customer> getAllCustomer() {
		List<Customer> customer = repo.findAll();
		return customer;
	}

	@Override
	public String deleteByCustomerId(int id) {
		Customer customer = repo.findByCustomerId(id);
		int customerid = customer.getCustomerId();
		String customername = customer.getName();
		repo.delete(customer);
		return "The Data for the Customer id : " + customerid + "\nCustomer Name :" + customername
				+ "\n Deleted Successfully !!!";
	}

	@Override
	public CustomerPolicy getCustPolyCombo(Integer cid) {
		List<PolicyDTO> policydto = policyclient.getCollection(cid);
		Optional<Customer> opt = repo.findById(cid);
		Customer customer = opt.get();
		CustomerPolicy customerpolicy = new CustomerPolicy();
		customerpolicy.setCust(customer);
		customerpolicy.setPoly(policydto);
		return customerpolicy;

	}

	@Override
	public List<Customer> getCustomerInfoForClaim(Integer cid) {
//		List<Customer> customer=repo.find(cid);
		return null;
	}

	@Override
	public void fileClaim(ClaimDTO claim) {
	  claimclient.fileClaim(claim);
	}

}
