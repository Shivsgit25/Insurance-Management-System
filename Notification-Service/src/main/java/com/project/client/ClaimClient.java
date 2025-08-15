package com.project.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.project.DTO.ClaimDTO;

@FeignClient(name="CLAIMSERVICE", path="/api/claims")
public interface ClaimClient {

	 @GetMapping("/{claimId}")
	    public ResponseEntity<ClaimDTO> getClaimById(@PathVariable Integer claimId);
	
}
