package com.project;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import com.project.exception.ClaimNotFoundException;
import com.project.model.Claim;
import com.project.repository.ClaimRepository;
import com.project.service.ClaimServiceImpl;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class InsuranceClaimProcessingApplicationTests {

	@Mock
	private ClaimRepository claimRepository;

	@InjectMocks
	private ClaimServiceImpl claimService;

	@Test
	void testGetClaimById_ReturnsClaim() throws ClaimNotFoundException {
		Claim claim = new Claim();
		claim.setClaimId(1);

		Mockito.when(claimRepository.findById(1)).thenReturn(Optional.of(claim));

		Claim result = claimService.getClaimById(1);

		Assertions.assertEquals(1, result.getClaimId());
	}

}
