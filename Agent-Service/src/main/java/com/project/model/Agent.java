package com.project.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "agent")
public class Agent {

	@Id
	int agentId;
	String contactInfo;
    String name;
    int policyId;
    int claimId;
    int customerId;
    
    
//    @ElementCollection
//    @CollectionTable(name="agent_policies", joinColumns=@JoinColumn(name="agentId"))
//    @Column(name = "policy_id")
//    List<Integer> assignedPolicies;


	public Agent(int agentId, String name, String contactInfo,int policyId,int claimId,int customerId) {
		super();
		this.agentId = agentId;
		this.name = name;
		this.contactInfo = contactInfo;
		this.policyId=policyId;
		this.claimId=claimId;
		this.customerId=customerId;
		
//		this.assignedPolicies=assignedPolicies;

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

	public int getPolicyId() {
		return policyId;
	}

	public void setPolicyId(int policyId) {
		this.policyId = policyId;
	}

	public int getClaimId() {
		return claimId;
	}

	public void setClaimId(int claimId) {
		this.claimId = claimId;
	}

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	


	
}
