package com.project.model;

public class ClaimDTO {
	
	private Integer claimId;
	private Integer policyId;
	private Integer customerId;
	private Double claimAmount;
	private Integer agentId;
	public enum ClaimStatus {
	    FILED,
	    UNDER_REVIEW,
	    APPROVED,
	    REJECTED
	}

	public Integer getClaimId() {
		return claimId;
	}
	public void setClaimId(Integer claimId) {
		this.claimId = claimId;
	}
	public Integer getPolicyId() {
		return policyId;
	}
	public void setPolicyId(Integer policyId) {
		this.policyId = policyId;
	}
	public Integer getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}
	public Double getClaimAmount() {
		return claimAmount;
	}
	public void setClaimAmount(Double claimAmount) {
		this.claimAmount = claimAmount;
	}
	public Integer getAgentId() {
		return agentId;
	}
	public void setAgentId(Integer agentId) {
		this.agentId = agentId;
	}
	public ClaimDTO(Integer claimId, Integer policyId, Integer customerId, Double claimAmount, Integer agentId) {
		super();
		this.claimId = claimId;
		this.policyId = policyId;
		this.customerId = customerId;
		this.claimAmount = claimAmount;
		this.agentId = agentId;
	}
	public ClaimDTO() {
		super();
	}
	
	

}
