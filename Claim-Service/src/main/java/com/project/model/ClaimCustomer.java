package com.project.model;

import java.util.List;

public class ClaimCustomer {
	
	private Claim claims;
	private List<CustomerDTO> customer;

	public Claim getClaim() {
		return claims;
	}
	public void setClaim(Claim claims) {
		this.claims = claims;
	}
	public List<CustomerDTO> getCustomer() {
		return customer;
	}
	public void setCustomer(List<CustomerDTO> customer) {
		this.customer = customer;
	}
	
	public ClaimCustomer() {
		super();
	}
	public ClaimCustomer(Claim claims, List<CustomerDTO> customer) {
		super();
		this.claims = claims;
		this.customer = customer;
	}
	
	
	
}
