package com.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.project.clients")

public class InsuranceClaimProcessingApplication {
	public static void main(String[] args) {
		SpringApplication.run(InsuranceClaimProcessingApplication.class, args);
	}
}
