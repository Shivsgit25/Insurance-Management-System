// File: com.project.controller.CustomerController.java

package com.project.controller;

import java.util.List;

import org.apache.http.auth.InvalidCredentialsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.model.ClaimDTO;
import com.project.model.Customer;
import com.project.model.CustomerPolicy;
import com.project.service.CustomerService;

/**
 * @RestController: A convenience annotation that combines @Controller and @ResponseBody.
 * @RequestMapping: Maps web requests to a specific handler class or method.
 */
@RestController
@RequestMapping("/customer")
public class CustomerController {

    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    private final CustomerService service;

    /**
     * Constructor for dependency injection of CustomerService.
     * @param service: The CustomerService dependency to be injected.
     */
    public CustomerController(CustomerService service) {
        this.service = service;
    }

    /**
     * @PostMapping: Maps HTTP POST requests to the specified URI.
     * @RequestBody: Binds the HTTP request body to the Customer object.
     * Adds a new customer to the system.
     * @param customer: The customer object to add.
     * @Return: A success message from the service layer.
     */
    @PostMapping("/add")
    public String addCustomer(@RequestBody Customer customer) {
        logger.info("############################# Running AddCustomer function ######################################");
        logger.info("Adding customer: {}", customer);
        return service.addCustomer(customer);
    }

    /**
     * @PostMapping: Handles POST requests for customer login.
     * Authenticates a customer with their email and password.
     * @param loginCustomer: The customer object containing login credentials.
     * @Return: A welcome message upon successful login.
     * @throws InvalidCredentialsException: Thrown if login credentials are invalid.
     */
    @PostMapping("/login")
    public String login(@RequestBody Customer loginCustomer) throws InvalidCredentialsException {
        logger.info("Attempting to login user with email: {}", loginCustomer.getEmail());
        return service.loginCustomer(loginCustomer.getEmail(), loginCustomer.getPassword());
    }

    /**
     * @PostMapping: Handles POST requests for customer updates.
     * Updates the details of an existing customer.
     * @param customer: The customer object with updated details.
     * @Return: A confirmation message of the update.
     */
    @PostMapping("/Update")
    public String updateCustomer(@RequestBody Customer customer) {
        logger.info("############################# Running UpdateCustomer function ######################################");
        logger.info("Updating customer: {}", customer);
        return service.updateCustomer(customer);
    }

    /**
     * @GetMapping: Maps HTTP GET requests.
     * @PathVariable: Extracts a value from the URI template.
     * Retrieves a customer by their unique ID.
     * @param id: The ID of the customer to retrieve.
     * @Return: The customer object.
     */
    @GetMapping("/getCustomer/{id}")
    public Customer getCustomerById(@PathVariable("id") int id) {
        logger.info("############################# Running getCustomer function ######################################");
        logger.info("Fetching customer by ID: {}", id);
        return service.getCustomerById(id);
    }

    /**
     * @PostMapping: Handles POST requests for customer deletion.
     * Deletes a customer by their ID.
     * @param id: The ID of the customer to delete.
     * @Return: A confirmation message of the deletion.
     */
    @PostMapping("/deleteCustomer/{id}")
    public String deleteCustomerById(@PathVariable("id") int id) {
        logger.info("############################# Running deleteCustomer function ######################################");
        logger.info("Deleting customer by ID: {}", id);
        return service.deleteByCustomerId(id);
    }

    /**
     * @GetMapping: Handles GET requests to retrieve all customers.
     * Fetches a list of all customers in the system.
     * @Return: A list of all customer objects.
     */
    @GetMapping("/getAllCustomer")
    public List<Customer> getAllCustomer() {
        logger.info("############################# Running getAllCustomer function ######################################");
        logger.info("Fetching all customers");
        return service.getAllCustomer();
    }

    /**
     * @GetMapping: Handles GET requests to combine customer and policy details.
     * Retrieves a customer's details along with their associated policies from an external service.
     * @param cid: The ID of the customer to get policy details for.
     * @Return: An object containing the customer and their policies.
     */
    @GetMapping("/getCustomerPolicyDetails/{cid}")
    public CustomerPolicy getPolicyDetailsOnCustomer(@PathVariable("cid") Integer cid) {
        logger.info("############################# Running getCustomerPolicyDetails function ######################################");
        logger.info("Fetching policy details for customer ID: {}", cid);
        return service.getCustPolyCombo(cid);
    }

    /**
     * @PostMapping: Handles POST requests to file a new claim.
     * Files a claim by sending the claim data to the service layer.
     * @param claim: The claim data transfer object to file.
     * @Return: A confirmation message that the claim was filed.
     */
    @PostMapping("/file")
    public String fileClaim(@RequestBody ClaimDTO claim) {
        logger.info("############################# Running file function for getting claim data ######################################");
        logger.info("Filing claim: {}", claim);
        service.fileClaim(claim);
        return "CLAIM FILED";
    }

    /**
     * @GetMapping: Handles GET requests for agents to view customer details.
     * Retrieves a customer by their ID for a view by an agent.
     * @param id: The ID of the customer to retrieve.
     * @Return: The customer object.
     */
    @GetMapping("/getCustomerForAgent/{id}")
    public Customer getCustomerForAgent(@PathVariable("id") Integer id) {
        logger.info("############################# Running getCustomerForAgent function ######################################");
        logger.info("Fetching customer for agent ID: {}", id);
        return service.getCustomerForAgent(id);
    }
    
    /**
     * @GetMapping: Handles GET requests to find a customer by email.
     * Retrieves a customer's details using their email address.
     * @param email: The email address of the customer.
     * @Return: The customer object.
     */
    @GetMapping("/getCustomerByEmail/{email}")
    public Customer getCustomerByEmail(@PathVariable("email") String email) {
    	logger.info("############################# Running getCustomerByEmail function in line number 74 ######################################");
        logger.info("Fetching customer for agent ID: {}", email);
        return service.getCustomerByEmail(email);
    }
}