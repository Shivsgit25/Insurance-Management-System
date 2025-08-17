package com.project.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.client.ClaimClient;
import com.project.client.PolicyClient;
import com.project.exception.CustomerNotFoundException;
import com.project.exception.ExternalServiceException;
import com.project.model.ClaimDTO;
import com.project.model.Customer;
import com.project.model.CustomerPolicy;
import com.project.model.PolicyDTO;
import com.project.repository.CustomerRepository;

import feign.FeignException;

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
        repo.findById(customer.getCustomerId())
            .orElseThrow(() -> new CustomerNotFoundException("Customer with ID " + customer.getCustomerId() + " not found for update."));
		repo.save(customer);
		return "Customer Updated Successfully on ID : " + customer.getCustomerId();
	}

	@Override
	public Customer getCustomerById(int id) {
		return repo.findById(id)
                   .orElseThrow(() -> new CustomerNotFoundException("Customer with ID " + id + " not found."));
	}

	@Override
	public List<Customer> getAllCustomer() {
		List<Customer> customer = repo.findAll();
		return customer;
	}

	@Override
	public String deleteByCustomerId(int id) {
        Customer customer = repo.findById(id)
                                .orElseThrow(() -> new CustomerNotFoundException("Customer with ID " + id + " not found for deletion."));
		repo.delete(customer);
		return "The Data for the Customer id : " + customer.getCustomerId() + "\nCustomer Name :" + customer.getName()
				+ "\n Deleted Successfully !!!";
	}

	@Override
	public CustomerPolicy getCustPolyCombo(Integer cid) {
        Customer customer = repo.findById(cid)
                                .orElseThrow(() -> new CustomerNotFoundException("Customer with ID " + cid + " not found to get policy details."));
        
        try {
            List<PolicyDTO> policydto = policyclient.getCollection(cid);
            CustomerPolicy customerpolicy = new CustomerPolicy();
            customerpolicy.setCust(customer);
            customerpolicy.setPoly(policydto);
            return customerpolicy;
        } catch (FeignException ex) {
            throw new ExternalServiceException("Failed to retrieve policy details.", ex);
        }
	}

	@Override
	public List<Customer> getCustomerInfoForClaim(Integer cid) {
		return null;
	}

	@Override
	public void fileClaim(ClaimDTO claim) {
        try {
            claimclient.fileClaim(claim);
        } catch (FeignException ex) {
            throw new ExternalServiceException("Failed to file a claim.", ex);
        }
	}

	public Customer getCustomerForAgent(Integer id) {
		return repo.findById(id)
                   .orElseThrow(() -> new CustomerNotFoundException("Customer with ID " + id + " not found for agent."));
	}
}