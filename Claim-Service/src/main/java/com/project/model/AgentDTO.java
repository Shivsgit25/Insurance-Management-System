package com.project.model;

import java.util.List;

public class AgentDTO {

	Integer agentId;
	String contactInfo;
	String name;
	List<Integer> assignedPolicies;

	public AgentDTO(Integer agentId, String name, String contactInfo, List<Integer> assignedPolicies) {
		super();
		this.agentId = agentId;
		this.name = name;
		this.contactInfo = contactInfo;
		this.assignedPolicies = assignedPolicies;

	}

	public AgentDTO() {
		super();
	}

	public int getAgentId() {
		return agentId;
	}

	public void setAgentId(Integer agentId) {
		this.agentId = agentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContactInfo() {
		return contactInfo;
	}

	public void setContactInfo(String contactInfo) {
		this.contactInfo = contactInfo;
	}

	public List<Integer> getAssignedPolicies() {
		return assignedPolicies;
	}

	public void setAssignedPolicies(List<Integer> assignedPolicies) {
		this.assignedPolicies = assignedPolicies;
	}
}
