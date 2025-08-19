package com.project.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PolicyAgent {
	private Policy policy;
	private List<AgentDTO> agent;
}
