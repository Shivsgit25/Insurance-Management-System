package com.project.DTO;



import java.time.LocalDate;

import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PolicyDTO {
    private Integer policyId;

    private String name;
    private Double premiumAmount;
    private String coverageDetails;
    private Integer validityPeriod;
    
    private Integer customerId;
    private Integer agentId;
    private Integer claimId;
    

    private LocalDate entryDate;
    private LocalDate expiryDate;

}
