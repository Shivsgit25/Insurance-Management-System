package com.project.model;

import java.util.List;

import lombok.Data;

@Data
public class PolicyCustomer {
	private Policy policy;
	private List<CustomerDTO> customer;

}
