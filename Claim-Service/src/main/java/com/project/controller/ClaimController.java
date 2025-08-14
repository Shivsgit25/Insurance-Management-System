package com.project.controller;

import com.project.exception.ClaimNotFoundException;
import com.project.model.Claim;
import com.project.service.ClaimService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for handling claim-related operations.
 */
@RestController
@RequestMapping("/api/claims")
public class ClaimController {

    private static final Logger logger = LoggerFactory.getLogger(ClaimController.class);

    private final ClaimService claimService;

    public ClaimController(ClaimService claimService) {
        this.claimService = claimService;
    }

    /**
     * Files a new insurance claim.
     */
    @PostMapping("/file")
    public ResponseEntity<Claim> fileClaim(@RequestBody Claim claim) {
        logger.info("Received request to file claim for policyId: {}", claim.getPolicyId());
        Claim savedClaim = claimService.fileClaim(claim);
        logger.info("Claim filed successfully with ID: {}", savedClaim.getClaimId());
        return ResponseEntity.ok(savedClaim);
    }

    /**
     * Updates the status of an existing claim.
     */
    @PutMapping("/{claimId}/status")
    public ResponseEntity<Claim> updateClaimStatus(@PathVariable Integer claimId,
                                                   @RequestParam Claim.Status status) {
        logger.info("Request to update status of claim ID: {} to {}", claimId, status);
        Claim updatedClaim = claimService.reviewClaim(claimId, status);
        if (updatedClaim == null) {
            logger.warn("Claim not found for ID: {}", claimId);
            throw new ClaimNotFoundException("Claim not found with ID: " + claimId);
        }
        logger.info("Claim status updated successfully for ID: {}", claimId);
        return ResponseEntity.ok(updatedClaim);
    }

    /**
     * Retrieves a claim by its ID.
     */
    @GetMapping("/{claimId}")
    public ResponseEntity<Claim> getClaimById(@PathVariable Integer claimId) {
        logger.info("Fetching claim with ID: {}", claimId);
        Claim claim = claimService.getClaimById(claimId);
        if (claim == null) {
            logger.warn("Claim not found for ID: {}", claimId);
            throw new ClaimNotFoundException("Claim not found with ID: " + claimId);
        }
        logger.info("Claim retrieved successfully for ID: {}", claimId);
        return ResponseEntity.ok(claim);
    }

    /**
     * Retrieves all claims filed by a specific customer.
     */
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Claim>> getClaimsByCustomer(@PathVariable Integer customerId) {
        logger.info("Fetching claims for customer ID: {}", customerId);
        List<Claim> claims = claimService.getClaimsByCustomer(customerId);
        if (claims == null || claims.isEmpty()) {
            logger.warn("No claims found for customer ID: {}", customerId);
            throw new ClaimNotFoundException("No claims found for customer ID: " + customerId);
        }
        logger.info("Found {} claims for customer ID: {}", claims.size(), customerId);
        return ResponseEntity.ok(claims);
    }
}
