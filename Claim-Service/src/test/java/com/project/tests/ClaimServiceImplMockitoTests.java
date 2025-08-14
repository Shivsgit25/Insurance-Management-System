package com.project.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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
class ClaimServiceImplMockitoTest {

    @InjectMocks
    private ClaimServiceImpl claimService;

    @Mock
    private ClaimRepository claimRepository;

    private Claim sampleClaim;

    @BeforeEach
    void setUp() {
        sampleClaim = new Claim();
        sampleClaim.setClaimId(1);
        sampleClaim.setPolicyId(101);
        sampleClaim.setCustomerId(201);
        sampleClaim.setAgentId(301);
        sampleClaim.setClaimAmount(7500.0);
        sampleClaim.setStatus(Status.FILED);
    }

    @Test
    void testFileClaim_SavesWithFiledStatus() {
        when(claimRepository.save(any())).thenReturn(sampleClaim);

        Claim result = claimService.fileClaim(sampleClaim);
        assertNotNull(result);
        assertEquals(Status.FILED, result.getStatus());
        verify(claimRepository, times(1)).save(any());
    }

    @Test
    void testReviewClaim_UpdatesStatus() {
        when(claimRepository.findById(1)).thenReturn(Optional.of(sampleClaim));
        when(claimRepository.save(any())).thenReturn(sampleClaim);

        Claim updated = claimService.reviewClaim(1, Status.APPROVED);
        assertEquals(Status.APPROVED, updated.getStatus());
        verify(claimRepository).save(any());
    }

    @Test
    void testReviewClaim_ThrowsExceptionIfNotFound() {
        when(claimRepository.findById(99)).thenReturn(Optional.empty());
        assertThrows(ClaimNotFoundException.class, () -> claimService.reviewClaim(99, Status.REJECTED));
    }

    @Test
    void testGetClaimById_Found() {
        when(claimRepository.findById(1)).thenReturn(Optional.of(sampleClaim));
        Claim result = claimService.getClaimById(1);
        assertEquals(1, result.getClaimId());
    }

    @Test
    void testGetClaimById_NotFound() {
        when(claimRepository.findById(999)).thenReturn(Optional.empty());
        assertThrows(ClaimNotFoundException.class, () -> claimService.getClaimById(999));
    }

    @Test
    void testGetClaimsByCustomer_ReturnsList() {
        when(claimRepository.findByCustomerId(201)).thenReturn(List.of(sampleClaim));
        List<Claim> claims = claimService.getClaimsByCustomer(201);
        assertEquals(1, claims.size());
    }

    @Test
    void testGetAllClaims_ReturnsList() {
        when(claimRepository.findAll()).thenReturn(List.of(sampleClaim));
        List<Claim> claims = claimService.getAllClaims();
        assertFalse(claims.isEmpty());
    }

    @Test
    void testGetClaimsByStatus_ReturnsList() {
        when(claimRepository.findByStatus(Status.FILED)).thenReturn(List.of(sampleClaim));
        List<Claim> claims = claimService.getClaimsByStatus(Status.FILED);
        assertEquals(Status.FILED, claims.get(0).getStatus());
    }
}
