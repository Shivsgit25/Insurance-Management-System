package com.project.model;

import java.util.List;

public class AgentCustomer {
	
	private Agent agent;
	private List<CustomerDTO> cust;
	public Agent getAgent() {
		return agent;
	}
	public void setAgent(Agent agent) {
		this.agent = agent;
	}
	public List<CustomerDTO> getCust() {
		return cust;
	}
	public void setCust(List<CustomerDTO> cust) {
		this.cust = cust;
	}
	

}
