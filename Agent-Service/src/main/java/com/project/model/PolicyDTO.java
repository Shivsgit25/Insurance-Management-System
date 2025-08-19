package com.project.model;

import com.fasterxml.jackson.annotation.JsonInclude;

public class PolicyDTO {
	
	private Integer policyId;

    private String name;
    private Double premiumAmount;
    private String coverageDetails;
    private Integer validityPeriod;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private CustomerDTO customer;
    
	public PolicyDTO(Integer policyId, String name, Double premiumAmount, String coverageDetails,
			Integer validityPeriod, CustomerDTO customer) {
		super();
		this.policyId = policyId;
		this.name = name;
		this.premiumAmount = premiumAmount;
		this.coverageDetails = coverageDetails;
		this.validityPeriod = validityPeriod;
		this.customer = customer;
	}
	public CustomerDTO getCustomer() {
		return customer;
	}
	public void setCustomer(CustomerDTO customer) {
		this.customer = customer;
	}
	public Integer getPolicyId() {
		return policyId;
	}
	public void setPolicyId(Integer policyId) {
		this.policyId = policyId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Double getPremiumAmount() {
		return premiumAmount;
	}
	public void setPremiumAmount(Double premiumAmount) {
		this.premiumAmount = premiumAmount;
	}
	public String getCoverageDetails() {
		return coverageDetails;
	}
	public void setCoverageDetails(String coverageDetails) {
		this.coverageDetails = coverageDetails;
	}
	public Integer getValidityPeriod() {
		return validityPeriod;
	}
	public void setValidityPeriod(Integer validityPeriod) {
		this.validityPeriod = validityPeriod;
	}
	
	public PolicyDTO() {
		super();
	}
    
    

}
