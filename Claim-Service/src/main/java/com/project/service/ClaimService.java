package com.project.service;

import com.project.model.Claim;
import java.util.List;

/**
 * Service interface for claim operations. Defines the business logic methods
 * for managing insurance claims.
 */
public interface ClaimService {

	/**
	 * Files a new insurance claim. Validates and persists the claim details.
	 *
	 * @param claim the claim object containing policy, customer, and amount
	 *              information
	 * @return the saved Claim entity with generated ID and initial status
	 */
	Claim fileClaim(Claim claim);

	/**
	 * Updates the status of an existing claim. Typically used during the review
	 * process to approve or reject a claim.
	 *
	 * @param claimId the ID of the claim to be updated
	 * @param status  the new status to assign (e.g., APPROVED, REJECTED)
	 * @return the updated Claim entity with the new status
	 */
	Claim reviewClaim(Long claimId, Claim.Status status);

	/**
	 * Retrieves a claim by its unique ID. Useful for viewing detailed claim
	 * information.
	 *
	 * @param claimId the ID of the claim to retrieve
	 * @return the Claim entity if found
	 */
	Claim getClaimById(Long claimId);

	/**
	 * Retrieves all claims filed by a specific customer. Enables customer-centric
	 * views of claim history.
	 *
	 * @param customerId the ID of the customer
	 * @return a list of Claim entities associated with the customer
	 */
	List<Claim> getClaimsByCustomer(Long customerId);
}
