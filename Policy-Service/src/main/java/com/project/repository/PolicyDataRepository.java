package com.project.repository;

import com.project.model.Policydata;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PolicyDataRepository extends JpaRepository<Policydata, Integer> {
}
