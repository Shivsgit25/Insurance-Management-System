package com.project.model;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDTO {
	
	private Integer customerId;
	private String name;
	private String email;
	private String gender;
	private Date date;
	private Long aadharnumber;
	private Long phone;
	private String address;
	private String role;

}
