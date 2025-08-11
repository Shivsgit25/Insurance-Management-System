package com.project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.model.Claim;
import com.project.service.ClaimService;

/**
 * REST controller for handling claim-related operations. Provides endpoints for
 * filing claims, updating status, and retrieving claim data.
 */
@RestController
@RequestMapping("/api/claims")
public class ClaimController {


	
	@Autowired
	private  ClaimService claimService;

	/**
	 * Constructor-based injection of ClaimService.
	 * 
	 * @param claimService service layer for claim business logic
	 */
//	@Autowired
//	public ClaimController(ClaimService claimService) {
//		this.claimService = claimService;
//	}

	/**
	 * Endpoint to file a new insurance claim. Accepts claim details in the request
	 * body and returns the saved claim.
	 *
	 * @param claim the claim object to be filed
	 * @return ResponseEntity containing the saved Claim
	 */
	@PostMapping("/file")
	public ResponseEntity<Claim> fileClaim(@RequestBody Claim claim) {
		return ResponseEntity.ok(claimService.fileClaim(claim));
	}

	/**
	 * Endpoint to update the status of an existing claim. Accepts claim ID as a
	 * path variable and new status as a request parameter.
	 *
	 * @param id     the ID of the claim to be updated
	 * @param status the new status to assign (e.g., APPROVED, REJECTED)
	 * @return ResponseEntity containing the updated Claim
	 */
	@PutMapping("/{id}/status")
	public ResponseEntity<Claim> updateClaimStatus(@PathVariable Long id, @RequestParam Claim.Status status) {
		return ResponseEntity.ok(claimService.reviewClaim(id, status));
	}

	/**
	 * Endpoint to retrieve a claim by its ID. Returns the claim details if found.
	 *
	 * @param id the ID of the claim to retrieve
	 * @return ResponseEntity containing the Claim
	 */
	@GetMapping("/{id}")
	public ResponseEntity<Claim> getClaimById(@PathVariable Long id) {
		return ResponseEntity.ok(claimService.getClaimById(id));
	}

	/**
	 * Endpoint to retrieve all claims filed by a specific customer. Accepts
	 * customer ID as a path variable.
	 *
	 * @param customerId the ID of the customer
	 * @return ResponseEntity containing a list of Claims
	 */
	@GetMapping("/customer/{customerId}")
	public ResponseEntity<List<Claim>> getClaimsByCustomer(@PathVariable Long customerId) {
		return ResponseEntity.ok(claimService.getClaimsByCustomer(customerId));
	}
}
