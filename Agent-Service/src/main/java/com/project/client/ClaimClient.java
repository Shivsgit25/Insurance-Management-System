package com.project.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import com.project.model.ClaimDTO;

@FeignClient(name="CLAIMSERVICE", path="/api/claims")
public interface ClaimClient {
	
	@GetMapping("/claims/all")
	public ResponseEntity<List<ClaimDTO>> getAllClaims();

}
