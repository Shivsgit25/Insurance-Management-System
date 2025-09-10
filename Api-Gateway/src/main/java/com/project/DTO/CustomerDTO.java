package com.project.DTO;

import java.sql.Date;

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
	private String gender;
	private Date date;
	private Long aadharnumber;
	private Long phone;
	private String address;
	private String role;

}
