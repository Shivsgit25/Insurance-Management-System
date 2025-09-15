// File: com.project.service.ClaimServiceImpl.java

package com.project.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.clients.NotificationClient;
import com.project.exception.ClaimNotFoundException;
import com.project.model.Claim;
import com.project.repository.ClaimRepository;

/**
 * @Service: Marks this class as a Spring service, a high-level component in the
 *           service layer.
 */
@Service
public class ClaimServiceImpl implements ClaimService {

	private static final Logger logger = LoggerFactory.getLogger(ClaimServiceImpl.class);

	private final ClaimRepository claimRepository;

	/**
	 * Constructor for dependency injection of ClaimRepository.
	 * 
	 * @param claimRepository: The repository for claim data.
	 */
	public ClaimServiceImpl(ClaimRepository claimRepository) {
		this.claimRepository = claimRepository;
		logger.info("ClaimServiceImpl initialized");
	}

	@Autowired
	private NotificationClient notificationClient;

	/**
	 * @Override: Indicates that this method is overriding a method from the
	 *            ClaimService interface. Files a new claim, sets its initial status
	 *            to FILED, and saves it to the database.
	 * @param claim: The claim object to be saved.
	 * @Return: The saved Claim object with its new ID.
	 */
	@Override
	public Claim fileClaim(Claim claim) {
		logger.info("Filing new claim for policyId: {}", claim.getPolicyId());
		claim.setStatus(Claim.Status.FILED);
		Claim saved = claimRepository.save(claim);
		logger.info("Claim filed with ID: {}", saved.getClaimId());
		//notificationClient.claimFilled(claim);
		return saved;
	}

	/**
	 * @Override: Updates the status of an existing claim.
	 * @param claimId: The ID of the claim to be updated.
	 * @param status:  The new status to be set.
	 * @Return: The updated Claim object.
	 * @throws ClaimNotFoundException: Thrown if the claim with the given ID is not
	 *                                 found.
	 */
	@Override
	public Claim reviewClaim(Integer claimId, Claim.Status status) throws ClaimNotFoundException {
		logger.info("Reviewing claim ID: {} with new status: {}", claimId, status);
		Claim claim = claimRepository.findById(claimId).orElseThrow(() -> {
			logger.warn("Claim not found with ID: {}", claimId);
			return new ClaimNotFoundException("Claim not found with ID: " + claimId);
		});
		claim.setStatus(status);
		Claim updated = claimRepository.save(claim);
		logger.info("Claim ID: {} updated to status: {}", claimId, status);
		//notificationClient.claimUpdated(claimId, status);
		return updated;
	}

	/**
	 * @Override: Retrieves a claim by its unique ID.
	 * @param claimId: The unique ID of the claim to retrieve.
	 * @Return: The claim object corresponding to the given ID.
	 * @throws ClaimNotFoundException: Thrown if the claim is not found.
	 */
	@Override
	public Claim getClaimById(Integer claimId) throws ClaimNotFoundException {
		logger.info("Fetching claim with ID: {}", claimId);
		return claimRepository.findById(claimId).orElseThrow(() -> {
			logger.warn("Claim not found with ID: {}", claimId);
			return new ClaimNotFoundException("Claim not found with ID: " + claimId);
		});
	}

	/**
	 * @Override: Fetches a list of claims for a specific customer.
	 * @param customerId: The ID of the customer whose claims to retrieve.
	 * @Return: A list of claims associated with the customer.
	 */
	@Override
	public List<Claim> getClaimsByCustomer(Integer customerId) {
		logger.info("Fetching claims for customer ID: {}", customerId);
		List<Claim> claims = claimRepository.findByCustomerId(customerId);
		logger.info("Found {} claims for customer ID: {}", claims.size(), customerId);
		return claims;
	}

	/**
	 * @Override: Fetches a list of all claims from the repository.
	 * @Return: A list of all claim objects.
	 */
	@Override
	public List<Claim> getAllClaims() {
		logger.info("Fetching all claims");
		List<Claim> claims = claimRepository.findAll();
		logger.info("Total claims found: {}", claims.size());
		return claims;
	}

	/**
	 * @Override: Fetches a list of claims filtered by a specific status.
	 * @param status: The status to filter the claims by.
	 * @Return: A list of claims with the specified status.
	 */
	@Override
	public List<Claim> getClaimsByStatus(Claim.Status status) {
		logger.info("Fetching claims with status: {}", status);
		List<Claim> claims = claimRepository.findByStatus(status);
		logger.info("Found {} claims with status: {}", claims.size(), status);
		return claims;
	}
}