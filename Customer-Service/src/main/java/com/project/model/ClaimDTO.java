package com.project.model;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
@Data
public class ClaimDTO {
	private Integer claimId;
	private Integer policyId;
	private Integer customerId;

	private Double claimAmount;
	private Integer agentId;
	@Enumerated(EnumType.STRING)
	private Status status;

	public enum Status {
		FILED, // Claim has been filed
		UNDER_REVIEW, // Claim is being reviewed
		APPROVED, // Claim has been approved
		REJECTED // Claim has been rejected
	}

}
