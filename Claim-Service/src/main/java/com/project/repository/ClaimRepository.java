package com.project.repository;

import com.project.model.Claim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Claim entity. Provides CRUD operations and custom
 * query methods for accessing claim data.
 */
@Repository
public interface ClaimRepository extends JpaRepository<Claim, Integer> {

	List<Claim> findByCustomerId(Integer customerId);

	List<Claim> findByStatus(Claim.Status status);

	List<Claim> findByAgentId(Integer customerId);

}
