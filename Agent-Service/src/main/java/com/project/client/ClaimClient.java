package com.project.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

//import com.project.model.Claim
import com.project.model.ClaimDTO;

@FeignClient(name="CLAIMSERVICE", path="/api/claims")
public interface ClaimClient {
	
	@GetMapping("/claims/all")
	public ResponseEntity<List<ClaimDTO>> getAllClaims();
	
	
	
	@PutMapping("/{claimId}/status")
	public ResponseEntity<ClaimDTO> updateClaimStatus(@PathVariable Integer claimId,
	                                                   @RequestParam ClaimDTO.ClaimStatus status);



    @GetMapping("/claims/{claimId}")
    ResponseEntity<ClaimDTO> getClaimById(@PathVariable Integer claimId);
	
	

}
