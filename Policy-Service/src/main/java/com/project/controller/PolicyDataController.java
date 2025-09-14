package com.project.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.model.Policydata;
import com.project.service.PolicyDataService;

@RestController
@RequestMapping("/api/policylist")
public class PolicyDataController {


    private PolicyDataService policydataService;

    public PolicyDataController(PolicyDataService policydataService) {
		this.policydataService = policydataService;
	}
    
    
    // Get all available policies
    @GetMapping
    public List<Policydata> getAllPolicies() {
        return policydataService.getAllPolicies();
    }

    // Add a new policy
    @PostMapping
    public Policydata addPolicy(@RequestBody Policydata policydata) {
        return policydataService.addPolicy(policydata);
    }

    // Get a policy by ID
    @GetMapping("/{id}")
    public Policydata getPolicyById(@PathVariable Integer id) {
        return policydataService.getPolicyById(id);
    }

    // Delete a policy
    @DeleteMapping("/{id}")
    public void deletePolicy(@PathVariable Integer id) {
        policydataService.deletePolicy(id);
    }

	
}
