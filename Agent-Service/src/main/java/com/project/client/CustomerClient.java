package com.project.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name="Customer-Service", path="/policies")
public interface CustomerClient {

}
