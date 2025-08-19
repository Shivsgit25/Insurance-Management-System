package com.project.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "claim_info")
public class Claim {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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
