package com.project.model;

import java.util.List;

public class ClaimPolicy {

	private Claim claims;
	private List<PolicyDTO> policy;

	public Claim getClaim() {
		return claims;
	}

	public void setClaim(Claim claims) {
		this.claims = claims;
	}

	public List<PolicyDTO> getPolicy() {
		return policy;
	}

	public void setPolicy(List<PolicyDTO> policy) {
		this.policy = policy;
	}

	public ClaimPolicy() {
		super();
	}

	public ClaimPolicy(Claim claims, List<PolicyDTO> policy) {
		super();
		this.claims = claims;
		this.policy = policy;
	}

}
