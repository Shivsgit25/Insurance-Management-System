package com.project.model;



import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
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
    private String policyType;
    private Double premiumAmount;
    private String coverageDetails;
    private Integer validityPeriod;
    
    private Integer customerId;
    private Integer agentId;
   
    

    private LocalDate entryDate;
    private LocalDate expiryDate;

    @PrePersist
    public void setDates() {
        this.entryDate = LocalDate.now();
        if (validityPeriod != null) {
            this.expiryDate = entryDate.plusYears(validityPeriod);
        }
    }

    
}
