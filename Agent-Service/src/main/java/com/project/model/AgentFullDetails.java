package com.project.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

public class AgentFullDetails {
	
	private Agent agent;
	private List<PolicyDTO> agentPolicies;
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private List<PolicyDTO> customerPolicies;
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private CustomerDTO customer;
	
	public CustomerDTO getCustomer() {
		return customer;
	}
	public void setCustomer(CustomerDTO customer) {
		this.customer = customer;
	}
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
