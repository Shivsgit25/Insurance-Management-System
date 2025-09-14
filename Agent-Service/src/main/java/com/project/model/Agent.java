package com.project.model;

import java.sql.Date;

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

//	private Integer agentId;
//	private String contactInfo;
//	private String password;
//
//	private String name;
//    private Integer policyId;
//    private Integer claimId;
//    private Integer customerId;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer agentId;
	private String name;
	private String contactInfo;
	private String orgEmail;
	private String password;
	private String gender;
	private Date date;
	private Long aadharnumber;
	private Long phone;
	private String address;
	private String role;
    


    


	
}
