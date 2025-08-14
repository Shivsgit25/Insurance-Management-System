package com.project.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.project.model.ClaimDTO;


@FeignClient
(name = "CLAIMSERVICE",path ="/api/claims")
public interface ClaimClient {
	 @PostMapping("/file")
	    public ResponseEntity<ClaimDTO> fileClaim(@RequestBody ClaimDTO claim);
}
