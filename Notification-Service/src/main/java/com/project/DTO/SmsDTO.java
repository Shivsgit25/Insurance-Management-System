package com.project.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

 @Data // Generates getters, setters, toString, equals, hashCode
 @NoArgsConstructor // Generates a no-argument constructor
 @AllArgsConstructor // Generates a constructor with all fields
public class SmsDTO {
    private String to;   // Recipient's phone number (e.g., "+1234567890")
    private String body; // Content of the SMS message
}