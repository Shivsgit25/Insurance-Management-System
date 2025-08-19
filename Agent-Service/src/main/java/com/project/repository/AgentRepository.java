package com.project.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.model.Agent;

@Repository
public interface AgentRepository extends JpaRepository<Agent, Integer> {

	List<Agent> findByPolicyId(Integer policyId);

	

	Optional<Agent> findByCustomerId(Integer customerId);



	Agent findByContactInfo(String contactInfo);

}