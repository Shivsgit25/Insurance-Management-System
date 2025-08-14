package com.project.model;

import java.util.List;

public class ClaimAgent {
	
	private Claim claims;
	private List<AgentDTO> agent;
	
	public Claim getClaims() {
		return claims;
	}
	public void setClaim(Claim claims) {
		this.claims = claims;
	}
	public List<AgentDTO> getAgent() {
		return agent;
	}
	public void setAgent(List<AgentDTO> agent) {
		this.agent = agent;
	}
	public ClaimAgent(Claim claims, List<AgentDTO> agent) {
		super();
		this.claims = claims;
		this.agent = agent;
	}
	public ClaimAgent() {
		super();
	}
	
	
	
}
