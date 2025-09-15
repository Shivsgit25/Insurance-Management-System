package com.project.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class InappNotificationDTO {
	private String subject;
	private String type;
	private String details;
	private Integer customerId;
	private Integer agentId;
}
