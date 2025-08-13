package com.project.model;

import lombok.Data;

@Data
public class PolicyDTO {
	private Integer policyId;
    private String name;
    private Double premiumAmount;
    private String coverageDetails;
    private Integer validityPeriod;
    // New field to hold the customer ID
    private Integer customerId;
}