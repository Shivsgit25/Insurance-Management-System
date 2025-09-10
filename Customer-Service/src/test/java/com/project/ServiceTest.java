package com.project;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.project.client.ClaimClient;
import com.project.client.PolicyClient;
import com.project.exception.CustomerNotFoundException;
import com.project.exception.ExternalServiceException;
import com.project.model.ClaimDTO;
import com.project.model.Customer;
import com.project.model.CustomerPolicy;
import com.project.model.PolicyDTO;
import com.project.repository.CustomerRepository;
import com.project.service.CusotmerServiceImpl;

import feign.FeignException;

class ServiceTest {

	@InjectMocks
	CusotmerServiceImpl customerService;

	@Mock
	CustomerRepository repo;

	@Mock
	PolicyClient policyclient;

	@Mock
	ClaimClient claimclient;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testUpdateCustomer_Success() {
		Customer customer = new Customer();
		customer.setCustomerId(1);
		when(repo.findById(1)).thenReturn(Optional.of(customer));
		when(repo.save(customer)).thenReturn(customer);

		String result = customerService.updateCustomer(customer);
		assertEquals("Customer Updated Successfully on ID : 1", result);
		verify(repo, times(1)).findById(1);
		verify(repo, times(1)).save(customer);
	}

	@Test
	void testUpdateCustomer_CustomerNotFound() {
		Customer customer = new Customer();
		customer.setCustomerId(1);
		when(repo.findById(1)).thenReturn(Optional.empty());

		CustomerNotFoundException thrown = assertThrows(CustomerNotFoundException.class, () -> {
			customerService.updateCustomer(customer);
		});

		assertEquals("Customer with ID 1 not found for update.", thrown.getMessage());
		verify(repo, times(1)).findById(1);
	}

	@Test
	void testGetCustomerById_Success() {
		Customer customer = new Customer();
		customer.setCustomerId(1);
		when(repo.findById(1)).thenReturn(Optional.of(customer));

		Customer result = customerService.getCustomerById(1);
		assertNotNull(result);
		assertEquals(1, result.getCustomerId());
		verify(repo, times(1)).findById(1);
	}

	@Test
	void testGetCustomerById_CustomerNotFound() {
		when(repo.findById(1)).thenReturn(Optional.empty());

		CustomerNotFoundException thrown = assertThrows(CustomerNotFoundException.class, () -> {
			customerService.getCustomerById(1);
		});

		assertEquals("Customer with ID 1 not found.", thrown.getMessage());
		verify(repo, times(1)).findById(1);
	}

	@Test
	void testGetAllCustomer_Success() {
		List<Customer> customers = Arrays.asList(new Customer(), new Customer());
		when(repo.findAll()).thenReturn(customers);

		List<Customer> result = customerService.getAllCustomer();
		assertNotNull(result);
		assertEquals(2, result.size());
		verify(repo, times(1)).findAll();
	}

	@Test
	void testDeleteByCustomerId_Success() {
		Customer customer = new Customer();
		customer.setCustomerId(1);
		customer.setName("John Doe");
		when(repo.findById(1)).thenReturn(Optional.of(customer));
		doNothing().when(repo).delete(customer);

		String result = customerService.deleteByCustomerId(1);
		assertEquals("The Data for the Customer id : 1\nCustomer Name :John Doe\n Deleted Successfully !!!", result);
		verify(repo, times(1)).findById(1);
		verify(repo, times(1)).delete(customer);
	}

	@Test
	void testDeleteByCustomerId_CustomerNotFound() {
		when(repo.findById(1)).thenReturn(Optional.empty());

		CustomerNotFoundException thrown = assertThrows(CustomerNotFoundException.class, () -> {
			customerService.deleteByCustomerId(1);
		});

		assertEquals("Customer with ID 1 not found for deletion.", thrown.getMessage());
		verify(repo, times(1)).findById(1);
	}

	@Test
	void testGetCustPolyCombo_Success() {
		Integer customerId = 1;
		Customer customer = new Customer();
		customer.setCustomerId(customerId);
		PolicyDTO policy1 = new PolicyDTO();
		PolicyDTO policy2 = new PolicyDTO();
		List<PolicyDTO> policies = Arrays.asList(policy1, policy2);

		when(repo.findById(customerId)).thenReturn(Optional.of(customer));
		when(policyclient.getCollection(customerId)).thenReturn(policies);

		CustomerPolicy result = customerService.getCustPolyCombo(customerId);

		assertNotNull(result);
		assertEquals(customer, result.getCust());
		assertEquals(policies, result.getPoly());
		verify(repo, times(1)).findById(customerId);
		verify(policyclient, times(1)).getCollection(customerId);
	}

	@Test
	void testGetCustPolyCombo_CustomerNotFound() {
		Integer customerId = 1;
		when(repo.findById(customerId)).thenReturn(Optional.empty());

		CustomerNotFoundException thrown = assertThrows(CustomerNotFoundException.class, () -> {
			customerService.getCustPolyCombo(customerId);
		});

		assertEquals("Customer with ID 1 not found to get policy details.", thrown.getMessage());
		verify(repo, times(1)).findById(customerId);
	}

	@Test
	void testGetCustPolyCombo_ExternalServiceException() {
		Integer customerId = 1;
		Customer customer = new Customer();
		customer.setCustomerId(customerId);
		when(repo.findById(customerId)).thenReturn(Optional.of(customer));
		when(policyclient.getCollection(customerId)).thenThrow(FeignException.class);

		ExternalServiceException thrown = assertThrows(ExternalServiceException.class, () -> {
			customerService.getCustPolyCombo(customerId);
		});

		assertEquals("Failed to retrieve policy details.", thrown.getMessage());
		verify(repo, times(1)).findById(customerId);
		verify(policyclient, times(1)).getCollection(customerId);
	}

	
	@Test
	void testFileClaim_ExternalServiceException() {
		ClaimDTO claim = new ClaimDTO();
		doThrow(FeignException.class).when(claimclient).fileClaim(claim);

		ExternalServiceException thrown = assertThrows(ExternalServiceException.class, () -> {
			customerService.fileClaim(claim);
		});

		assertEquals("Failed to file a claim.", thrown.getMessage());
		verify(claimclient, times(1)).fileClaim(claim);
	}

	@Test
	void testGetCustomerForAgent_Success() {
		Integer customerId = 1;
		Customer customer = new Customer();
		customer.setCustomerId(customerId);
		when(repo.findById(customerId)).thenReturn(Optional.of(customer));

		Customer result = customerService.getCustomerForAgent(customerId);
		assertNotNull(result);
		assertEquals(customerId, result.getCustomerId());
		verify(repo, times(1)).findById(customerId);
	}

	@Test
	void testGetCustomerForAgent_CustomerNotFound() {
		Integer customerId = 1;
		when(repo.findById(customerId)).thenReturn(Optional.empty());

		CustomerNotFoundException thrown = assertThrows(CustomerNotFoundException.class, () -> {
			customerService.getCustomerForAgent(customerId);
		});

		assertEquals("Customer with ID 1 not found for agent.", thrown.getMessage());
		verify(repo, times(1)).findById(customerId);
	}
}