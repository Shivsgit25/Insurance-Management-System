package com.project;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class CustomerServiceApplication {

    private static final Logger logger = LoggerFactory.getLogger(CustomerServiceApplication.class);

    public static void main(String[] args) {
        logger.info("############# Starting CustomerServiceApplication...##################");
        SpringApplication.run(CustomerServiceApplication.class, args);
    }
}
