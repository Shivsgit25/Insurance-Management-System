package com.project.repository;

import com.project.model.Claim;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * Repository interface for Claim entity. Provides CRUD operations and custom
 * query methods for accessing claim data.
 */
public interface ClaimRepository extends JpaRepository<Claim, Integer> {

	List<Claim> findByCustomerId(Integer customerId);

	List<Claim> findByStatus(Claim.Status status);

}
