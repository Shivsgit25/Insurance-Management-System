package com.project.model;

import java.time.LocalDate;

public class PolicyDTO {

	private String name;
	private Double premiumAmount;
	private String coverageDetails;
	private LocalDate validityPeriod;
	private Integer customerId;
	private Integer agentId;

	private Integer policyId;

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

	public LocalDate getValidityPeriod() {
		return validityPeriod;
	}

	public void setValidityPeriod(LocalDate validityPeriod) {
		this.validityPeriod = validityPeriod;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public Integer getAgentId() {
		return agentId;
	}

	public void setAgentId(Integer agentId) {
		this.agentId = agentId;
	}

	public PolicyDTO(Integer policyId, String name, Double premiumAmount, String coverageDetails, LocalDate validityPeriod,
			Integer customerId, Integer agentId) {
		super();
		this.policyId = policyId;
		this.name = name;
		this.premiumAmount = premiumAmount;
		this.coverageDetails = coverageDetails;
		this.validityPeriod = validityPeriod;
		this.customerId = customerId;
		this.agentId = agentId;
	}

	public PolicyDTO() {
		super();
	}

}
