package com.project;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.project.exception.ClaimNotFoundException;
import com.project.model.Claim;
import com.project.model.Claim.Status;
import com.project.repository.ClaimRepository;
import com.project.service.ClaimServiceImpl;

@ExtendWith(MockitoExtension.class)
class ClaimServiceImplTest {

	@Mock
	private ClaimRepository claimRepository;

	@InjectMocks
	private ClaimServiceImpl claimService;

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
	void fileClaim_SetsStatusAndSaves() {
		when(claimRepository.save(any())).thenReturn(sampleClaim);

		Claim result = claimService.fileClaim(sampleClaim);
		assertEquals(Status.FILED, result.getStatus());
		assertEquals(7500.0, result.getClaimAmount());
	}

	@Test
	void reviewClaim_UpdatesStatusAndSaves() {
		sampleClaim.setStatus(Status.APPROVED);
		when(claimRepository.findById(1)).thenReturn(Optional.of(sampleClaim));
		when(claimRepository.save(any())).thenReturn(sampleClaim);

		Claim result = claimService.reviewClaim(1, Status.APPROVED);
		assertEquals(Status.APPROVED, result.getStatus());
	}

	@Test
	void reviewClaim_NotFound_ThrowsException() {
		when(claimRepository.findById(99)).thenReturn(Optional.empty());

		assertThrows(ClaimNotFoundException.class, () -> claimService.reviewClaim(99, Status.REJECTED));
	}

	@Test
	void getClaimById_Found_ReturnsClaim() {
		when(claimRepository.findById(1)).thenReturn(Optional.of(sampleClaim));

		Claim result = claimService.getClaimById(1);
		assertEquals(1, result.getClaimId());
	}

	@Test
	void getClaimById_NotFound_ThrowsException() {
		when(claimRepository.findById(999)).thenReturn(Optional.empty());

		assertThrows(ClaimNotFoundException.class, () -> claimService.getClaimById(999));
	}

	@Test
	void getClaimsByCustomer_ReturnsList() {
		when(claimRepository.findByCustomerId(201)).thenReturn(List.of(sampleClaim));

		List<Claim> result = claimService.getClaimsByCustomer(201);
		assertEquals(1, result.size());
		assertEquals(201, result.get(0).getCustomerId());
	}

	@Test
	void getAllClaims_ReturnsList() {
		when(claimRepository.findAll()).thenReturn(List.of(sampleClaim));

		List<Claim> result = claimService.getAllClaims();
		assertEquals(1, result.size());
	}

	@Test
	void getClaimsByStatus_ReturnsList() {
		when(claimRepository.findByStatus(Status.FILED)).thenReturn(List.of(sampleClaim));

		List<Claim> result = claimService.getClaimsByStatus(Status.FILED);
		assertEquals(Status.FILED, result.get(0).getStatus());
	}
}
