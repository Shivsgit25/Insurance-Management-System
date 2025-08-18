package com.project.model;

import java.util.List;

public class AgentFullDetails {
	
	private Agent agent;
	private List<PolicyDTO> agentPolicies;
	private List<PolicyDTO> customerPolicies;
	public Agent getAgent() {
		return agent;
	}
	public void setAgent(Agent agent) {
		this.agent = agent;
	}
	public List<PolicyDTO> getAgentPolicies() {
		return agentPolicies;
	}
	public void setAgentPolicies(List<PolicyDTO> agentPolicies) {
		this.agentPolicies = agentPolicies;
	}
	public List<PolicyDTO> getCustomerPolicies() {
		return customerPolicies;
	}
	public void setCustomerPolicies(List<PolicyDTO> customerPolicies) {
		this.customerPolicies = customerPolicies;
	}
	
	
	

}
