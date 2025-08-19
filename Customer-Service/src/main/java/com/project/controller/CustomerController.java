package com.project.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.project.model.ClaimDTO;
import com.project.model.Customer;
import com.project.model.CustomerPolicy;
import com.project.service.CustomerService;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    @Autowired
    CustomerService service;

    @PostMapping("/add")
    public String AddCustomer(@RequestBody Customer customer) {
    	logger.info("############################# Running AddCustomer function in line number 26 ######################################");
        logger.info("Adding customer: {}", customer);
        return service.AddCustomer(customer);
    }

    @PostMapping("/Update")
    public String UpdateCustomer(@RequestBody Customer customer) {
    	logger.info("############################# Running UpdateCustomer function in line number 33 ######################################");
        logger.info("Updating customer: {}", customer);
        return service.UpdateCustomer(customer);
    }

    @GetMapping("/getCustomer/{id}")
    public Customer getCustomerById(@PathVariable("id") int id) {
    	logger.info("############################# Running getCustomer function in line number 40 ######################################");
        logger.info("Fetching customer by ID: {}", id);
        return service.getCustomerById(id);
    }

    @PostMapping("/deleteCustomer/{id}")
    public String deleteCustomerById(@PathVariable("id") int id) {
    	logger.info("############################# Running deleteCustomer function in line number 45 ######################################");
        logger.info("Deleting customer by ID: {}", id);
        return service.deleteByCustomerId(id);
    }

    @GetMapping("/getAllCustomer")
    public List<Customer> getAllCustomer() {
    	logger.info("############################# Running getAllCustomer function in line number 52 ######################################");
        logger.info("Fetching all customers");
        return service.getAllCustomer();
    }

    @GetMapping("/getCustomerPolicyDetails/{cid}")
    public CustomerPolicy getPolicyDetailsOnCustomer(@PathVariable("cid") Integer cid) {
    	logger.info("############################# Running getCustomerPolicyDetails function in line number 59 ######################################");
        logger.info("Fetching policy details for customer ID: {}", cid);
        return service.getCustPolyCombo(cid);
    }

    @PostMapping("/file")
    public String fileClaim(@RequestBody ClaimDTO claim) {
    	logger.info("############################# Running file function for getting claim data in line number 66 ######################################");
        logger.info("Filing claim: {}", claim);
        service.fileClaim(claim);
        return "CLAIM FILED";
    }

    @GetMapping("/getCustomerForAgent/{id}")
    public Customer getCustomerForAgent(@PathVariable("id") Integer id) {
    	logger.info("############################# Running getCustomerForAgent function in line number 74 ######################################");
        logger.info("Fetching customer for agent ID: {}", id);
        return service.getCustomerForAgent(id);
    }
    @GetMapping("/getCustomerByEmail/{email}")
    public Customer getCustomerByEmail(@PathVariable("email") String email) {
    	logger.info("############################# Running getCustomerByEmail function in line number 74 ######################################");
        logger.info("Fetching customer for agent ID: {}", email);
        return service.getCustomerByEmail(email);
    }
}
