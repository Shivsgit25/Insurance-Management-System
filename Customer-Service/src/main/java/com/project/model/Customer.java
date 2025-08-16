package com.project.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "customer_info_service")
public class Customer {
	@Id
	private Integer customerId;
	private String name;
	private String email;
	private String password;
	private Long phone;
	private String address;
}
