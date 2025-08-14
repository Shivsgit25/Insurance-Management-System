package com.project.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import com.project.controller.ClaimController;
import com.project.exception.ClaimNotFoundException;
import com.project.model.Claim;
import com.project.model.Claim.Status;
import com.project.service.ClaimService;

class ClaimControllerJUnitTest {

    @InjectMocks
    private ClaimController claimController;

    @Mock
    private ClaimService claimService;

    private Claim sampleClaim;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sampleClaim = new Claim();
        sampleClaim.setClaimId(1);
        sampleClaim.setPolicyId(101);
        sampleClaim.setCustomerId(201);
        sampleClaim.setAgentId(301);
        sampleClaim.setClaimAmount(7500.0);
        sampleClaim.setStatus(Status.FILED);
    }

    @Test
    void testFileClaim_ReturnsOk() {
        when(claimService.fileClaim(any())).thenReturn(sampleClaim);
        ResponseEntity<Claim> response = claimController.fileClaim(sampleClaim);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(1, response.getBody().getClaimId());
    }

    @Test
    void testGetClaimById_Found() {
        when(claimService.getClaimById(1)).thenReturn(sampleClaim);
        ResponseEntity<Claim> response = claimController.getClaimById(1);
        assertEquals(Status.FILED, response.getBody().getStatus());
    }

    @Test
    void testGetClaimById_NotFound() {
        when(claimService.getClaimById(99)).thenReturn(null);
        assertThrows(ClaimNotFoundException.class, () -> claimController.getClaimById(99));
    }

    @Test
    void testUpdateClaimStatus_Success() {
        sampleClaim.setStatus(Status.APPROVED);
        when(claimService.reviewClaim(1, Status.APPROVED)).thenReturn(sampleClaim);
        ResponseEntity<Claim> response = claimController.updateClaimStatus(1, Status.APPROVED);
        assertEquals(Status.APPROVED, response.getBody().getStatus());
    }

    @Test
    void testUpdateClaimStatus_NotFound() {
        when(claimService.reviewClaim(99, Status.REJECTED)).thenReturn(null);
        assertThrows(ClaimNotFoundException.class, () -> claimController.updateClaimStatus(99, Status.REJECTED));
    }

    @Test
    void testGetClaimsByCustomer_Found() {
        when(claimService.getClaimsByCustomer(201)).thenReturn(List.of(sampleClaim));
        ResponseEntity<List<Claim>> response = claimController.getClaimsByCustomer(201);
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testGetClaimsByCustomer_NotFound() {
        when(claimService.getClaimsByCustomer(999)).thenReturn(List.of());
        assertThrows(ClaimNotFoundException.class, () -> claimController.getClaimsByCustomer(999));
    }

    @Test
    void testGetAllClaims_Found() {
        when(claimService.getAllClaims()).thenReturn(List.of(sampleClaim));
        ResponseEntity<List<Claim>> response = claimController.getAllClaims();
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    void testGetAllClaims_Empty() {
        when(claimService.getAllClaims()).thenReturn(List.of());
        assertThrows(ClaimNotFoundException.class, () -> claimController.getAllClaims());
    }

    @Test
    void testGetClaimsByStatus_Found() {
        when(claimService.getClaimsByStatus(Status.FILED)).thenReturn(List.of(sampleClaim));
        ResponseEntity<List<Claim>> response = claimController.getFiledClaims();
        assertEquals(Status.FILED, response.getBody().get(0).getStatus());
    }

    @Test
    void testGetClaimsByStatus_NotFound() {
        when(claimService.getClaimsByStatus(Status.REJECTED)).thenReturn(List.of());
        assertThrows(ClaimNotFoundException.class, () -> claimController.getRejectedClaims());
    }
}
