package com.project.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "agents")
public class Agent {

	@Id
	int agentId;
	String contactInfo;
    String name;


	public Agent(int agentId, String name, String contactInfo) {
		super();
		this.agentId = agentId;
		this.name = name;
		this.contactInfo = contactInfo;

	}

	public Agent() {
		super();
	}

	public int getAgentId() {
		return agentId;
	}

	public void setAgentId(int agentId) {
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

//	public List<Long> getAssignedPolicies() {
//		return assignedPolicies;
//	}
//
//	public void setAssignedPolicies(List<Long> assignedPolicies) {
//		this.assignedPolicies = assignedPolicies;
//	}
}
