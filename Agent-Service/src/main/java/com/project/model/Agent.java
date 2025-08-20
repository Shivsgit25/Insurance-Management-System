package com.project.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "agent")
public class Agent {

	@Id
	private Integer agentId;
	private String contactInfo;
	private String password;

	private String name;
    private Integer policyId;
    private Integer claimId;
    private Integer customerId;
    


    


	
}
