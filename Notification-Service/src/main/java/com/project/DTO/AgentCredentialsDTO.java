package com.project.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgentCredentialsDTO {
	private String contactInfo;
	private String orgEmail;
	private String password;
	private String name;
}
