package com.project.DTO;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgentDTO {
	private Integer agentId;
	private String name;
	private String contactInfo;
	private Long aadharnumber;
	private Long phone;
	private String address;
	private String role;

}
