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
@Table(name = "policydata")
public class Policydata {
    @Id
    @GeneratedValue
    private Integer policyId;
    private String name;
    private String policyType;
    private Double premiumAmount;
    private Integer coverageamount;
    private String coverageDetails;
}
