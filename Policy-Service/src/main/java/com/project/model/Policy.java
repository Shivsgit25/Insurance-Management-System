package com.project.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "policy_info")
public class Policy {
    @Id
    @GeneratedValue
    private Long policyId;

    private String name;
    private Double premiumAmount;
    private String coverageDetails;
    private LocalDate validityPeriod;
    
    private Long customerId;
    private Long agentId;


//    @ManyToOne
//    @JoinColumn(name = "customer_id")
//    private Customer customer;
    
//    @ManyToOne
//    @JoinColumn(name = "agent_id")
//    private Agent agent;
    
}
