// File: com.project.service.CustomerService.java

package com.project.service;
 
import java.util.Collections;
import java.util.List;

import org.apache.http.auth.InvalidCredentialsException;
import org.springframework.stereotype.Service;

import com.project.client.ClaimClient;
import com.project.client.NotificationClient;
import com.project.client.PolicyClient;
import com.project.exception.CustomerAlreadyExistsException;
import com.project.exception.CustomerNotFoundException;
import com.project.exception.ExternalServiceException;
import com.project.model.ClaimDTO;
import com.project.model.Customer;
import com.project.model.CustomerPolicy;
import com.project.model.PolicyDTO;
import com.project.repository.CustomerRepository;

import feign.FeignException;
 
/**
 * @Service: Marks this class as a Spring service, a high-level component in the service layer.
 */
@Service
public class CusotmerServiceImpl implements CustomerService {
    
    private static final String CUSTOMER_NOT_FOUND_MESSAGE = "Customer with ID %s not found.";
    private static final String CUSTOMER_ALREADY_EXISTS_MESSAGE = "Customer with email %s already exists.";


    private final CustomerRepository repo;
    private final PolicyClient policyclient;
    private final ClaimClient claimclient;
    private final NotificationClient notificationclient;
 
    /**
     * Constructor for dependency injection.
     * @param repo: Repository for customer data.
     * @param policyclient: Feign client for policy service.
     * @param claimclient: Feign client for claim service.
     * @param notificationclient: Feign client for notification service.
     */
    public CusotmerServiceImpl(CustomerRepository repo, PolicyClient policyclient, ClaimClient claimclient, NotificationClient notificationclient) {
        this.repo = repo;
        this.policyclient = policyclient;
        this.claimclient = claimclient;
        this.notificationclient = notificationclient;
    }
 
    /**
     * @Override: Indicates that this method is overriding a method from the CustomerService interface.
     * Adds a new customer to the database and sends a registration email.
     * @param customer: The customer object to be saved.
     * @Return: A success message indicating the customer was saved.
     */
    @Override
    public String addCustomer(Customer customer) {
        // Validation check for existing customer by email
        if (repo.findByEmail(customer.getEmail()) != null) {
            throw new CustomerAlreadyExistsException(String.format(CUSTOMER_ALREADY_EXISTS_MESSAGE, customer.getEmail()));
        }
        
        repo.save(customer);
        notificationclient.customerRegisteredMail(customer);
        return "Customer Saved Successfully";
    }
 
    /**
     * @Override: Updates an existing customer's details after verifying their existence.
     * @param customer: The customer object with updated information.
     * @Return: A success message with the updated customer's ID.
     */
    @Override
    public String updateCustomer(Customer customer) {
        repo.findById(customer.getCustomerId()).orElseThrow(() -> new CustomerNotFoundException(
                String.format(CUSTOMER_NOT_FOUND_MESSAGE, customer.getCustomerId()) + " for update."));
        repo.save(customer);
        return "Customer Updated Successfully on ID : " + customer.getCustomerId();
    }
 
    /**
     * @Override: Retrieves a customer by their unique ID, throwing an exception if not found.
     * @param id: The unique ID of the customer to retrieve.
     * @Return: The customer object corresponding to the given ID.
     */
    @Override
    public Customer getCustomerById(int id) {
        return repo.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(String.format(CUSTOMER_NOT_FOUND_MESSAGE, id)));
    }
 
    /**
     * @Override: Fetches a list of all customers from the repository.
     * @Return: A list of all customer objects.
     */
    @Override
    public List<Customer> getAllCustomer() {
        return repo.findAll();
    }
 
    /**
     * @Override: Deletes a customer by their ID after checking for their existence.
     * @param id: The unique ID of the customer to delete.
     * @Return: A confirmation message with the ID and name of the deleted customer.
     */
    @Override
    public String deleteByCustomerId(int id) {
        Customer customer = repo.findById(id).orElseThrow(
                () -> new CustomerNotFoundException(String.format(CUSTOMER_NOT_FOUND_MESSAGE, id) + " for deletion."));
        repo.delete(customer);
        return "The Data for the Customer id : " + customer.getCustomerId() + "\nCustomer Name :" + customer.getName()
                + "\n Deleted Successfully !!!";
    }
 
    /**
     * @Override: Combines customer details with their associated policies by calling an external service.
     * @param cid: The unique ID of the customer.
     * @Return: An object containing both the customer and their policy details.
     */
    @Override
    public CustomerPolicy getCustPolyCombo(Integer cid) {
        Customer customer = repo.findById(cid).orElseThrow(
                () -> new CustomerNotFoundException(String.format(CUSTOMER_NOT_FOUND_MESSAGE, cid) + " to get policy details."));
 
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
 
    /**
     * @Override: This method is currently not implemented and returns null.
     * @param cid: The unique ID of the customer.
     * @Return: An empty list as this method is not yet implemented.
     */
    @Override
    public List<Customer> getCustomerInfoForClaim(Integer cid) {
        return Collections.emptyList();
    }
 
    /**
     * @Override: Files a claim by sending the claim data to an micro-service of claims.
     * @param claim: The claim data transfer object to be filed.
     */
    @Override
    public void fileClaim(ClaimDTO claim) {
        try {
            claimclient.fileClaim(claim);
        } catch (FeignException ex) {
            throw new ExternalServiceException("Failed to file a claim.", ex);
        }
    }
 
    /**
     * Retrieves customer details for an agent's view by the customer's ID.
     * @param id: The unique ID of the customer.
     * @Return: The customer object corresponding to the given ID.
     */
    public Customer getCustomerForAgent(Integer id) {
        return repo.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(String.format(CUSTOMER_NOT_FOUND_MESSAGE, id) + " for agent."));
    }
 
    /**
     * @Override: Finds a customer by their unique email address.
     * @param email: The unique email address of the customer.
     * @Return: The customer object corresponding to the given email.
     */
    @Override
    public Customer getCustomerByEmail(String email) {
        return repo.findByEmail(email);
    }

    /**
     * @Override: Authenticates a customer by verifying their email and password.
     * @param email: The email address of the customer.
     * @param password: The password of the customer.
     * @Return: A welcome message with the customer's name upon successful login.
     * @throws InvalidCredentialsException: Thrown if the email or password do not match.
     */
    @Override
    public String loginCustomer(String email, String password) throws InvalidCredentialsException {
        Customer customer = repo.findByEmail(email);

        if (customer == null || !customer.getPassword().equals(password)) {
            throw new InvalidCredentialsException("Invalid email or password.");
        }

        return "Welcome Home, " + customer.getName() + "!";
    }
}