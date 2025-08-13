package com.project.model;



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
    private Integer policyId;

    private String name;
    private Double premiumAmount;
    private String coverageDetails;
    private Integer validityPeriod;
    
    private Integer customerId;
    private Integer agentId;
    private Integer claimId;


//    @ManyToOne
//    @JoinColumn(name = "customer_id")
//    private Customer customer;
    
//    @ManyToOne
//    @JoinColumn(name = "agent_id")
//    private Agent agent;
    
}
