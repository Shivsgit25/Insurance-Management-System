package com.project.repository;

import com.project.model.Claim;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * Repository interface for Claim entity. Provides CRUD operations and custom
 * query methods for accessing claim data.
 */
public interface ClaimRepository extends JpaRepository<Claim, Integer> {

	/**
	 * Retrieves all claims associated with a specific customer ID. Spring Data JPA
	 * automatically generates the query based on method name.
	 *
	 * @param customerId the ID of the customer whose claims are to be fetched
	 * @return a list of Claim entities filed by the given customer
	 */
	
	List<Claim> findByCustomerId(Integer customerId);
	
	List<Claim> findByStatus(Claim.Status status);


	// -------------------- Inherited Methods from JpaRepository
	// --------------------

	// save(S entity): Saves a given entity.
	// Optional<Claim> findById(Long id): Retrieves a claim by its ID.
	// List<Claim> findAll(): Retrieves all claims.
	// void deleteById(Long id): Deletes a claim by its ID.
	// boolean existsById(Long id): Checks if a claim exists by its ID.
	// long count(): Returns the total number of claims.

	// These methods are available without needing to declare them explicitly.
}
