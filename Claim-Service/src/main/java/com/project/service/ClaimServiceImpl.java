package com.project.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.exception.ClaimNotFoundException;
import com.project.model.Claim;
import com.project.repository.ClaimRepository;

@Service
public class ClaimServiceImpl implements ClaimService {

    private static final Logger logger = LoggerFactory.getLogger(ClaimServiceImpl.class);

    private final ClaimRepository claimRepository;

    @Autowired
    public ClaimServiceImpl(ClaimRepository claimRepository) {
        this.claimRepository = claimRepository;
        logger.info("ClaimServiceImpl initialized");
    }

    @Override
    public Claim fileClaim(Claim claim) {
        logger.info("Filing new claim for policyId: {}", claim.getPolicyId());
        claim.setStatus(Claim.Status.FILED);
        Claim saved = claimRepository.save(claim);
        logger.info("Claim filed with ID: {}", saved.getClaimId());
        return saved;
    }

    @Override
    public Claim reviewClaim(Integer claimId, Claim.Status status) throws ClaimNotFoundException {
        logger.info("Reviewing claim ID: {} with new status: {}", claimId, status);
        Claim claim = claimRepository.findById(claimId)
                .orElseThrow(() -> {
                    logger.warn("Claim not found with ID: {}", claimId);
                    return new ClaimNotFoundException("Claim not found with ID: " + claimId);
                });
        claim.setStatus(status);
        Claim updated = claimRepository.save(claim);
        logger.info("Claim ID: {} updated to status: {}", claimId, status);
        return updated;
    }

    @Override
    public Claim getClaimById(Integer claimId) throws ClaimNotFoundException {
        logger.info("Fetching claim with ID: {}", claimId);
        return claimRepository.findById(claimId)
                .orElseThrow(() -> {
                    logger.warn("Claim not found with ID: {}", claimId);
                    return new ClaimNotFoundException("Claim not found with ID: " + claimId);
                });
    }

    @Override
    public List<Claim> getClaimsByCustomer(Integer customerId) {
        logger.info("Fetching claims for customer ID: {}", customerId);
        List<Claim> claims = claimRepository.findByCustomerId(customerId);
        logger.info("Found {} claims for customer ID: {}", claims.size(), customerId);
        return claims;
    }

    @Override
    public List<Claim> getAllClaims() {
        logger.info("Fetching all claims");
     List<Claim> claims = claimRepository.findAll();
        logger.info("Total claims found: {}", claims.size());
        return claims;
    }

    @Override
    public List<Claim> getClaimsByStatus(Claim.Status status) {
        logger.info("Fetching claims with status: {}", status);
        List<Claim> claims = claimRepository.findByStatus(status);
        logger.info("Found {} claims with status: {}", claims.size(), status);
        return claims;
    }
}
