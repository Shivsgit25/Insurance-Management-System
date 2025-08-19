package com.project;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.project.controller.ClaimController;
import com.project.exception.ClaimNotFoundException;
import com.project.model.Claim;
import com.project.model.Claim.Status;
import com.project.service.ClaimService;

@ExtendWith(MockitoExtension.class)
class ClaimControllerTest {

	@Mock
	private ClaimService claimService;

	@InjectMocks
	private ClaimController claimController;

	private Claim sampleClaim;

	@BeforeEach
	void setup() {
		sampleClaim = new Claim();
		sampleClaim.setClaimId(1);
		sampleClaim.setPolicyId(101);
		sampleClaim.setCustomerId(201);
		sampleClaim.setAgentId(301);
		sampleClaim.setClaimAmount(7500.0);
		sampleClaim.setStatus(Status.FILED);
	}

	@Test
	void fileClaim_ReturnsClaim() {
		when(claimService.fileClaim(sampleClaim)).thenReturn(sampleClaim);

		var response = claimController.fileClaim(sampleClaim);
		assertEquals(200, response.getStatusCode().value());
		assertEquals(7500.0, response.getBody().getClaimAmount());
	}

	@Test
	void getClaimById_Found() {
		when(claimService.getClaimById(1)).thenReturn(sampleClaim);

		var response = claimController.getClaimById(1);
		assertEquals(1, response.getBody().getClaimId());
	}

	@Test
	void getClaimById_NotFound_ThrowsException() {
		when(claimService.getClaimById(99)).thenReturn(null);

		assertThrows(ClaimNotFoundException.class, () -> claimController.getClaimById(99));
	}

	@Test
	void updateClaimStatus_ReturnsUpdatedClaim() {
		sampleClaim.setStatus(Status.APPROVED);
		when(claimService.reviewClaim(1, Status.APPROVED)).thenReturn(sampleClaim);

		var response = claimController.updateClaimStatus(1, Status.APPROVED);
		assertEquals(Status.APPROVED, response.getBody().getStatus());
	}

	@Test
	void updateClaimStatus_NotFound_ThrowsException() {
		when(claimService.reviewClaim(99, Status.REJECTED)).thenReturn(null);

		assertThrows(ClaimNotFoundException.class, () -> claimController.updateClaimStatus(99, Status.REJECTED));
	}

	@Test
	void getClaimsByCustomer_ReturnsList() {
		when(claimService.getClaimsByCustomer(201)).thenReturn(List.of(sampleClaim));

		var response = claimController.getClaimsByCustomer(201);
		assertEquals(1, response.getBody().size());
		assertEquals(201, response.getBody().get(0).getCustomerId());
	}

	@Test
	void getClaimsByCustomer_NotFound_ThrowsException() {
		when(claimService.getClaimsByCustomer(999)).thenReturn(List.of());

		assertThrows(ClaimNotFoundException.class, () -> claimController.getClaimsByCustomer(999));
	}

	@Test
	void getAllClaims_ReturnsList() {
		when(claimService.getAllClaims()).thenReturn(List.of(sampleClaim));

		var response = claimController.getAllClaims();
		assertEquals(1, response.getBody().size());
	}

	@Test
	void getAllClaims_Empty_ThrowsException() {
		when(claimService.getAllClaims()).thenReturn(List.of());

		assertThrows(ClaimNotFoundException.class, () -> claimController.getAllClaims());
	}

	@Test
	void getClaimsByStatus_ReturnsList() {
		when(claimService.getClaimsByStatus(Status.FILED)).thenReturn(List.of(sampleClaim));

		var response = claimController.getFiledClaims();
		assertEquals(Status.FILED, response.getBody().get(0).getStatus());
	}

	@Test
	void getClaimsByStatus_Empty_ThrowsException() {
		when(claimService.getClaimsByStatus(Status.REJECTED)).thenReturn(List.of());

		assertThrows(ClaimNotFoundException.class, () -> claimController.getRejectedClaims());
	}
}
