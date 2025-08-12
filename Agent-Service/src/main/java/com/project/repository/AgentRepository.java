package com.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.model.Agent;

@Repository
public interface AgentRepository extends JpaRepository<Agent, Integer> {

}
