package com.project.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InAppDTO {
    private String userId;
    private String message;
    private String type; // e.g., "PolicyRenewal", "ClaimStatusUpdate", "Announcement"

    
}