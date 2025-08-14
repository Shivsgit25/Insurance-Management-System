package com.project.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDTO {
	private int customerId;
	private String name;
	private String email;
	private Long phone;
	private String address;
}
