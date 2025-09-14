package com.project.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.project.model.Policydata;
import com.project.repository.PolicyDataRepository;

@Service
public class PolicyDataServiceImpl implements PolicyDataService {

    private PolicyDataRepository policydataRepository;

    public PolicyDataServiceImpl(PolicyDataRepository policydataRepository) {
    	this.policydataRepository = policydataRepository;
	}
    
    @Override
    public List<Policydata> getAllPolicies() {
        return policydataRepository.findAll();
    }

    @Override
    public Policydata addPolicy(Policydata policydata) {
        return policydataRepository.save(policydata);
    }

    @Override
    public Policydata getPolicyById(Integer id) {
        return policydataRepository.findById(id).orElse(null);
    }

    @Override
    public void deletePolicy(Integer id) {
        policydataRepository.deleteById(id);
    }
}
