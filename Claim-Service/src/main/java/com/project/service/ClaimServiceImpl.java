package com.project.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.exception.ClaimNotFoundException;
import com.project.model.Claim;
import com.project.model.Claim.Status;
import com.project.repository.ClaimRepository;

/**
 * Implementation of ClaimService. Handles business logic for filing, reviewing,
 * and retrieving claims.
 */
@Service
public class ClaimServiceImpl implements ClaimService {

	private final ClaimRepository claimRepository;

	/**
	 * Constructor-based injection of ClaimRepository.
	 * 
	 * @param claimRepository repository for accessing claim data
	 */
	@Autowired
	public ClaimServiceImpl(ClaimRepository claimRepository) {
		this.claimRepository = claimRepository;
	}

	/**
	 * Files a new insurance claim. Sets the initial status to FILED and saves the
	 * claim to the database.
	 *
	 * @param claim the claim object containing policy, customer, and amount details
	 * @return the persisted Claim entity with generated ID and status
	 */
	@Override
	public Claim fileClaim(Claim claim) {
		claim.setStatus(Claim.Status.FILED); // Set default status
		return claimRepository.save(claim); // Save to database
	}

	/**
	 * Updates the status of an existing claim. Retrieves the claim by ID, throws
	 * exception if not found, and updates its status.
	 *
	 * @param claimId the ID of the claim to be reviewed
	 * @param status  the new status to assign (e.g., APPROVED, REJECTED)
	 * @return the updated Claim entity
	 * @throws ClaimNotFoundException if the claim ID does not exist
	 */
	@Override
	public Claim reviewClaim(Integer claimId, Claim.Status status) throws ClaimNotFoundException {
		Claim claim = claimRepository.findById(claimId)
				.orElseThrow(() -> new ClaimNotFoundException("Claim not found with ID: " + claimId));
		claim.setStatus(status); // Update status
		return claimRepository.save(claim); // Save changes
	}

	/**
	 * Retrieves a claim by its unique ID. Throws an exception if the claim is not
	 * found.
	 *
	 * @param claimId the ID of the claim to retrieve
	 * @return the Claim entity
	 * @throws ClaimNotFoundException if the claim ID does not exist
	 */
	@Override
	public Claim getClaimById(Integer claimId) throws ClaimNotFoundException {
		return claimRepository.findById(claimId)
				.orElseThrow(() -> new ClaimNotFoundException("Claim not found with ID: " + claimId));
	}

	/**
	 * Retrieves all claims filed by a specific customer. Uses a custom repository
	 * method to filter by customer ID.
	 *
	 * @param customerId the ID of the customer
	 * @return a list of Claim entities associated with the customer
	 */
	@Override
	public List<Claim> getClaimsByCustomer(Integer customerId) {
		return claimRepository.findByCustomerId(customerId);
	}

}
