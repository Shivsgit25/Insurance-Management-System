package com.project.model;

public class CustomerDTO {
	
	private String name;
	private String email;
	private Long phone;
	private String address;
	private Integer customerId;
	
	public int getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Long getPhone() {
		return phone;
	}
	public void setPhone(Long phone) {
		this.phone = phone;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public CustomerDTO(String name, String email, Long phone, String address, Integer customerId) {
		super();
		this.name = name;
		this.email = email;
		this.phone = phone;
		this.address = address;
		this.customerId = customerId;
	}
	public CustomerDTO() {
		super();
	}
	
	
}
