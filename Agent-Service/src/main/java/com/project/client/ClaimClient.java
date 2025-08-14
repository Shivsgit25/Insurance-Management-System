package com.project.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name="CLAIMSERVICE", path="/api/claims")
public interface ClaimClient {

}
