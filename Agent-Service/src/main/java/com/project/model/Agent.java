package com.project.model;

import jakarta.persistence.*;


@Entity
@Table(name = "agent")
public class Agent {

	@Id
	private Integer agentId;
	private String contactInfo;
    private String name;
    private Integer policyId;
    private Integer claimId;
    

    
    public Agent() {
		super();
	}
	public Agent(Integer agentId, String contactInfo, String name, Integer policyId, Integer claimId,
			Integer customerId) {
		super();
		this.agentId = agentId;
		this.contactInfo = contactInfo;
		this.name = name;
		this.policyId = policyId;
		this.claimId = claimId;
		this.customerId = customerId;
	}
	private Integer customerId;
	public Integer getAgentId() {
		return agentId;
	}
	public void setAgentId(Integer agentId) {
		this.agentId = agentId;
	}
	public String getContactInfo() {
		return contactInfo;
	}
	public void setContactInfo(String contactInfo) {
		this.contactInfo = contactInfo;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getPolicyId() {
		return policyId;
	}
	public void setPolicyId(Integer policyId) {
		this.policyId = policyId;
	}
	public Integer getClaimId() {
		return claimId;
	}
	public void setClaimId(Integer claimId) {
		this.claimId = claimId;
	}
	public Integer getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}
    
    


	
}
