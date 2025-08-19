package com.project.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDTO {
	private Integer customerId;
	private String name;
	private String email;
	private String password;
	private Long phone;
	private String address;
	private String role;

}
