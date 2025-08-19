package com.project.service;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import com.project.client.ClaimClient;
import com.project.client.NotificationClient;
import com.project.client.PolicyClient;
import com.project.exception.CustomerNotFoundException;
import com.project.exception.ExternalServiceException;
import com.project.exception.InvalidCredentialsException;
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

	@Autowired
	NotificationClient notificationclient;

	@Override
	public String AddCustomer(Customer customer) {
		repo.save(customer);
		notificationclient.customerRegisteredMail(customer);
		return "Customer Saved Successfully";

	}

	@Override
	public String UpdateCustomer(Customer customer) {
		repo.findById(customer.getCustomerId()).orElseThrow(() -> new CustomerNotFoundException(
				"Customer with ID " + customer.getCustomerId() + " not found for update."));
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
		Customer customer = repo.findById(id).orElseThrow(
				() -> new CustomerNotFoundException("Customer with ID " + id + " not found for deletion."));
		repo.delete(customer);
		return "The Data for the Customer id : " + customer.getCustomerId() + "\nCustomer Name :" + customer.getName()
				+ "\n Deleted Successfully !!!";
	}

	@Override
	public CustomerPolicy getCustPolyCombo(Integer cid) {
		Customer customer = repo.findById(cid).orElseThrow(
				() -> new CustomerNotFoundException("Customer with ID " + cid + " not found to get policy details."));

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

	@Override
	public Customer getCustomerByEmail(String email) {
		return repo.findByEmail(email);
	}

    private static final String CUSTOMER_NOT_FOUND_MSG = "Customer with ID %d not found.";
    private static final String CUSTOMER_NOT_FOUND_UPDATE_MSG = "Customer with ID %d not found for update.";
    private static final String CUSTOMER_NOT_FOUND_DELETE_MSG = "Customer with ID %d not found for deletion.";
    private static final String CUSTOMER_NOT_FOUND_POLICY_MSG = "Customer with ID %d not found to get policy details.";
    private static final String CUSTOMER_NOT_FOUND_AGENT_MSG = "Customer with ID %d not found for agent.";

    private final CustomerRepository repo;
    private final PolicyClient policyclient;
    private final ClaimClient claimclient;
    private final NotificationClient notificationclient;

    public CusotmerServiceImpl(CustomerRepository repo, PolicyClient policyclient, ClaimClient claimclient, NotificationClient notificationclient) {
        this.repo = repo;
        this.policyclient = policyclient;
        this.claimclient = claimclient;
        this.notificationclient = notificationclient;
    }

    @Override
    public String addCustomer(Customer customer) {
        repo.save(customer);
        notificationclient.customerRegisteredMail(customer);
        return "Customer Saved Successfully";
    }

    @Override
    public String updateCustomer(Customer customer) {
        repo.findById(customer.getCustomerId())
                .orElseThrow(() -> new CustomerNotFoundException(String.format(CUSTOMER_NOT_FOUND_UPDATE_MSG, customer.getCustomerId())));
        repo.save(customer);
        return "Customer Updated Successfully on ID : " + customer.getCustomerId();
    }

    @Override
    public Customer getCustomerById(int id) {
        return repo.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(String.format(CUSTOMER_NOT_FOUND_MSG, id)));
    }

    @Override
    public List<Customer> getAllCustomer() {
        return repo.findAll();
    }

    @Override
    public String deleteByCustomerId(int id) {
        Customer customer = repo.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(String.format(CUSTOMER_NOT_FOUND_DELETE_MSG, id)));
        repo.delete(customer);
        return String.format("The Data for the Customer id : %d%nCustomer Name :%s%n Deleted Successfully !!!",
                customer.getCustomerId(), customer.getName());
    }

    @Override
    public CustomerPolicy getCustPolyCombo(Integer cid) {
        Customer customer = repo.findById(cid)
                .orElseThrow(() -> new CustomerNotFoundException(String.format(CUSTOMER_NOT_FOUND_POLICY_MSG, cid)));

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
        // This method is intentionally left empty as it's not yet implemented.
        // It's a placeholder for future functionality to retrieve customer information
        // related to a specific claim ID, but the business logic for this is
        // not yet defined. Returning an empty list is a safe default behavior for now.
        return Collections.emptyList();
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
                .orElseThrow(() -> new CustomerNotFoundException(String.format(CUSTOMER_NOT_FOUND_AGENT_MSG, id)));
    }

    @Override
    public String loginCustomer(String email, String password) {
        Customer customer = repo.findByEmail(email);

        if (customer == null || !customer.getPassword().equals(password)) {
            throw new InvalidCredentialsException("Invalid email or password.");
        }

        return "Welcome Home, " + customer.getName() + "!";
    }
}