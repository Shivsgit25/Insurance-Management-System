package com.project.service;

import com.project.model.Claim;
import java.util.List;

/**
 * Service interface for claim operations. Defines the business logic methods
 * for managing insurance claims.
 */
public interface ClaimService {

	Claim fileClaim(Claim claim);

	Claim reviewClaim(Integer claimId, Claim.Status status);

	Claim getClaimById(Integer claimId);

	List<Claim> getClaimsByCustomer(Integer customerId);

	List<Claim> getAllClaims();

	List<Claim> getClaimsByStatus(Claim.Status status);

	List<Claim> getClaimsByAgent(Integer agentId);

}
