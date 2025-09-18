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
 * @RestController: A convenience annotation that combines @Controller
 *                  and @ResponseBody.
 * @RequestMapping: Maps web requests to a specific handler class or method.
 */
@RestController
@RequestMapping("/api/claims")
public class ClaimController {

	private static final Logger logger = LoggerFactory.getLogger(ClaimController.class);

	private final ClaimService claimService;

	/**
	 * Constructor for dependency injection of ClaimService.
	 * 
	 * @param claimService: The ClaimService dependency to be injected.
	 */
	public ClaimController(ClaimService claimService) {
		this.claimService = claimService;
		logger.info("ClaimController initialized");
	}

	/**
	 * @PostMapping: Handles HTTP POST requests for filing a new claim.
	 * @RequestBody: Binds the HTTP request body to the Claim object. Files a new
	 *               claim with the provided claim details.
	 * @param claim: The claim object to be filed.
	 * @Return: A ResponseEntity containing the saved Claim object.
	 */
	@PostMapping("/file")
	public ResponseEntity<Claim> fileClaim(@RequestBody Claim claim) {
		logger.info("Received request to file claim for policyId: {}", claim.getPolicyId());
		Claim savedClaim = claimService.fileClaim(claim);
		logger.info("Claim filed successfully with ID: {}", savedClaim.getClaimId());
		return ResponseEntity.ok(savedClaim);
	}

	/**
	 * @PutMapping: Handles HTTP PUT requests for updating a resource.
	 * @PathVariable: Extracts a value from the URI template.
	 * @RequestParam: Extracts a value from the query string. Updates the status of
	 *                an existing claim.
	 * @param claimId: The ID of the claim to update.
	 * @param status:  The new status for the claim.
	 * @Return: A ResponseEntity containing the updated Claim object.
	 * @throws ClaimNotFoundException: Thrown if the claim is not found.
	 */
	@PutMapping("/{claimId}/status")
	public ResponseEntity<Claim> updateClaimStatus(@PathVariable Integer claimId, @RequestParam Claim.Status status) {
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
	 * @GetMapping: Handles HTTP GET requests for retrieving a resource.
	 * @PathVariable: Extracts a value from the URI template. Retrieves a claim by
	 *                its unique ID.
	 * @param claimId: The ID of the claim to retrieve.
	 * @Return: A ResponseEntity containing the retrieved Claim object.
	 * @throws ClaimNotFoundException: Thrown if the claim is not found.
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
	 * @GetMapping: Handles HTTP GET requests for retrieving resources.
	 * @PathVariable: Extracts a value from the URI template. Retrieves a list of
	 *                all claims associated with a specific customer.
	 * @param customerId: The ID of the customer whose claims to retrieve.
	 * @Return: A ResponseEntity containing a list of Claim objects.
	 * @throws ClaimNotFoundException: Thrown if no claims are found for the
	 *                                 customer.
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

	/**
	 * @GetMapping: Handles HTTP GET requests to retrieve all claims. Fetches a list
	 *              of all claims in the system.
	 * @Return: A ResponseEntity containing a list of all Claim objects.
	 * @throws ClaimNotFoundException: Thrown if no claims are found in the system.
	 */
	@GetMapping("/claims/all")
	public ResponseEntity<List<Claim>> getAllClaims() {
		logger.info("Fetching all claims");
		List<Claim> claims = claimService.getAllClaims();
		if (claims == null || claims.isEmpty()) {
			logger.warn("No claims found");
			throw new ClaimNotFoundException("No claims found");
		}
		logger.info("Total claims retrieved: {}", claims.size());
		return ResponseEntity.ok(claims);
	}

	/**
	 * @GetMapping: Handles HTTP GET requests to retrieve claims with a specific
	 *              status. Retrieves all claims that have been filed but not yet
	 *              reviewed.
	 * @Return: A ResponseEntity containing a list of filed Claim objects.
	 * @throws ClaimNotFoundException: Thrown if no claims with this status are
	 *                                 found.
	 */
	@GetMapping("/status/filed")
	public ResponseEntity<List<Claim>> getFiledClaims() {
		return getClaimsByStatus(Claim.Status.FILED);
	}

	/**
	 * @GetMapping: Handles HTTP GET requests to retrieve claims with a specific
	 *              status. Retrieves all claims that are currently under review.
	 * @Return: A ResponseEntity containing a list of under-review Claim objects.
	 * @throws ClaimNotFoundException: Thrown if no claims with this status are
	 *                                 found.
	 */
	@GetMapping("/status/under-review")
	public ResponseEntity<List<Claim>> getUnderReviewClaims() {
		return getClaimsByStatus(Claim.Status.UNDER_REVIEW);
	}

	/**
	 * @GetMapping: Handles HTTP GET requests to retrieve claims with a specific
	 *              status. Retrieves all claims that have been approved.
	 * @Return: A ResponseEntity containing a list of approved Claim objects.
	 * @throws ClaimNotFoundException: Thrown if no claims with this status are
	 *                                 found.
	 */
	@GetMapping("/status/approved")
	public ResponseEntity<List<Claim>> getApprovedClaims() {
		return getClaimsByStatus(Claim.Status.APPROVED);
	}

	/**
	 * @GetMapping: Handles HTTP GET requests to retrieve claims with a specific
	 *              status. Retrieves all claims that have been rejected.
	 * @Return: A ResponseEntity containing a list of rejected Claim objects.
	 * @throws ClaimNotFoundException: Thrown if no claims with this status are
	 *                                 found.
	 */
	@GetMapping("/status/rejected")
	public ResponseEntity<List<Claim>> getRejectedClaims() {
		return getClaimsByStatus(Claim.Status.REJECTED);
	}

	/**
	 * Private helper method to retrieve claims based on their status.
	 * 
	 * @param status: The status to filter claims by.
	 * @Return: A ResponseEntity containing a list of claims with the specified
	 *          status.
	 * @throws ClaimNotFoundException: Thrown if no claims with the specified status
	 *                                 are found.
	 */
	private ResponseEntity<List<Claim>> getClaimsByStatus(Claim.Status status) {
		logger.info("Fetching claims with status: {}", status);
		List<Claim> claims = claimService.getClaimsByStatus(status);
		if (claims == null || claims.isEmpty()) {
			logger.warn("No claims found with status: {}", status);
			throw new ClaimNotFoundException("No claims found with status: " + status);
		}
		logger.info("Found {} claims with status: {}", claims.size(), status);
		return ResponseEntity.ok(claims);
	}
	
	
	@GetMapping("/agent/{agentId}")
	public ResponseEntity<List<Claim>> getClaimsByAgent(@PathVariable Integer agentId) {
		logger.info("Fetching claims for agent ID: {}", agentId);
		List<Claim> claims = claimService.getClaimsByAgent(agentId);
		if (claims == null || claims.isEmpty()) {
			logger.warn("No claims found for agent ID: {}", agentId);
			throw new ClaimNotFoundException("No claims found for agent ID: " + agentId);
		}
		logger.info("Found {} claims for agent ID: {}", claims.size(), agentId);
		return ResponseEntity.ok(claims);
	}
	
	
	
}