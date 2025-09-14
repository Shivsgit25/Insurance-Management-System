package com.project.service;

import com.project.model.Policydata;

import java.util.List;

public interface PolicyDataService {
    List<Policydata> getAllPolicies();
    Policydata addPolicy(Policydata policydata);
    Policydata getPolicyById(Integer id);
    void deletePolicy(Integer id);
}
