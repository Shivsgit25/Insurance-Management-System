package com.project.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgentDTO {
	private Integer agentId;
	private String contactInfo;
	private String name;
	private Integer policyId;
	private Integer claimId;

}
