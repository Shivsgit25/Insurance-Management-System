package com.project.model;

import java.util.List;

public class AgentClaim {
	
	private Agent agent;
	private List<ClaimDTO> claim;
	public Agent getAgent() {
		return agent;
	}
	public void setAgent(Agent agent) {
		this.agent = agent;
	}
	public List<ClaimDTO> getClaim() {
		return claim;
	}
	public void setClaim(List<ClaimDTO> claim) {
		this.claim = claim;
	}
	
	

}
