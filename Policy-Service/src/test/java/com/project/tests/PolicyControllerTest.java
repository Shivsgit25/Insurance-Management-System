package com.project.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.project.controller.PolicyController;
import com.project.exception.ResourceNotFoundException;
import com.project.model.Policy;
import com.project.service.PolicyService;

class PolicyControllerTest {

    @InjectMocks
    private PolicyController policyController;

    @Mock
    private PolicyService policyService;

    private Policy samplePolicy;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        samplePolicy = createSamplePolicy();
    }

    private Policy createSamplePolicy() {
        Policy policy = new Policy();
        policy.setPolicyId(1);
        policy.setName("Health Cover");
        policy.setPremiumAmount(5000.0);
        policy.setCoverageDetails("Full coverage");
        policy.setValidityPeriod(1);
        policy.setCustomerId(101);
        policy.setAgentId(201);
        return policy;
    }

    @Test
    void testGetPolicyById_Found() {
        when(policyService.getPolicyById(1)).thenReturn(samplePolicy);
        ResponseEntity<Policy> response = policyController.getPolicyById(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Health Cover", response.getBody().getName());
    }

    @Test
    void testGetPolicyById_NotFound() {
        when(policyService.getPolicyById(99)).thenThrow(new ResourceNotFoundException("Policy not found"));
        assertThrows(ResourceNotFoundException.class, () -> policyController.getPolicyById(99));
    }

    @Test
    void testGetPoliciesByCustomer_Found() {
        when(policyService.getPoliciesByCustomer(101)).thenReturn(List.of(samplePolicy));
        ResponseEntity<List<Policy>> response = policyController.getPoliciesByCustomer(101);
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testGetPoliciesByCustomer_NotFound() {
        when(policyService.getPoliciesByCustomer(999)).thenThrow(new ResourceNotFoundException("No policies found"));
        assertThrows(ResourceNotFoundException.class, () -> policyController.getPoliciesByCustomer(999));
    }

    @Test
    void testGetPoliciesByAgent_Found() {
        when(policyService.getPoliciesByAgent(201)).thenReturn(List.of(samplePolicy));
        ResponseEntity<List<Policy>> response = policyController.getPoliciesByAgent(201);
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testGetPoliciesByAgent_NotFound() {
        when(policyService.getPoliciesByAgent(888)).thenThrow(new ResourceNotFoundException("No policies found"));
        assertThrows(ResourceNotFoundException.class, () -> policyController.getPoliciesByAgent(888));
    }

    @Test
    void testGetAllPolicies_Found() {
        when(policyService.getAllPolicies()).thenReturn(List.of(samplePolicy));
        ResponseEntity<List<Policy>> response = policyController.getAllPolicies();
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    void testGetAllPolicies_Empty() {
        when(policyService.getAllPolicies()).thenThrow(new ResourceNotFoundException("No policies available"));
        assertThrows(ResourceNotFoundException.class, () -> policyController.getAllPolicies());
    }

    @Test
    void testGetCustomerPolicyDetails_Found() {
        when(policyService.getallpoliciesbycustomerId(101)).thenReturn(List.of(samplePolicy));
        List<Policy> response = policyController.getCollection(101);
        assertNotNull(response);
        assertEquals(1, response.size());
    }

    @Test
    void testGetCustomerPolicyDetails_NotFound() {
        when(policyService.getallpoliciesbycustomerId(999)).thenThrow(new ResourceNotFoundException("No policies found"));
        assertThrows(ResourceNotFoundException.class, () -> policyController.getCollection(999));
    }

    @Test
    void testGetPoliciesTesting_Found() {
        when(policyService.getallpoliciesbyagentId(201)).thenReturn(List.of(samplePolicy));
        List<Policy> response = policyController.getPolicies(201);
        assertNotNull(response);
        assertEquals(1, response.size());
    }

    @Test
    void testGetPoliciesTesting_NotFound() {
        when(policyService.getallpoliciesbyagentId(999)).thenThrow(new ResourceNotFoundException("No policies found"));
        assertThrows(ResourceNotFoundException.class, () -> policyController.getPolicies(999));
    }
}
