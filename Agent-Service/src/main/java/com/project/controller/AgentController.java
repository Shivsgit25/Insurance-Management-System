package com.project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.model.Agent;
import com.project.model.AgentClaim;
import com.project.model.AgentFullDetails;
import com.project.model.AgentPolicy;
import com.project.model.ClaimDTO;
import com.project.model.CustomerDTO;
import com.project.service.AgentService;

import ch.qos.logback.classic.Logger;
@RestController
@RequestMapping("/agents")
public class AgentController {
	
	@Autowired
	private AgentService ser;

	@PostMapping("/save")
	public String createAgent(@RequestBody Agent agent) {
	    return ser.createAgent(agent);
	}
	
	@GetMapping("/all")
	public ResponseEntity<List<Agent>> getAllAgents(){
		return ResponseEntity.ok(ser.getAllAgents());
	}
	
	@GetMapping("get/{agentId}")
	public ResponseEntity<Agent> getAgentById(@PathVariable Integer agentId){
		return ser.getAgentById(agentId)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
				
	}
	
    @PutMapping("/update/{agentId}")
	public ResponseEntity<Agent> updateAgent(@PathVariable Integer agentId, @RequestBody Agent agent){
		try {
		Agent updated = ser.updateAgent(agentId,agent);
		return ResponseEntity.ok(updated);
		} catch(Exception e) {
			return ResponseEntity.notFound().build();
		}
		
	}
    
    @DeleteMapping("/delete/{agentId}")
    public String deleteAgent(@PathVariable Integer agentId) {
    	ser.deleteAgent(agentId);
    	return "Agent Deleted";
    }
	
    
    //get agent by policyId
    @GetMapping("/policy/{policyId}")
    public ResponseEntity<List<Agent>> getAgentByPolicy(@PathVariable Integer policyId){
    	List<Agent> agents = ser.getAgentByPolicy(policyId);
    	return ResponseEntity.ok(agents);
    }
    
    @GetMapping("/getAgentPolicyDetails/{aid}")
	public AgentPolicy test(@PathVariable("aid") Integer aid) {
		
    	return ser.getAgentPolyCombo(aid);
	}
    
    @GetMapping("/getCustomerForAgent/{cid}")
	public CustomerDTO getCustomerForAgent(@PathVariable("cid") Integer cid) {
		
    	return ser.getCustomerForAgent(cid);
	}
    
   
    

    
    @GetMapping("/claims/all")
	public ResponseEntity<AgentClaim> getAllClaims(){
    	return ResponseEntity.ok(ser.getAllClaims());
    }
    
    @PutMapping("/approve-claim/{claimId}")
    public ResponseEntity<ClaimDTO> approveClaim(@PathVariable Integer claimId) {
        ClaimDTO updatedClaim = ser.approveClaim(claimId);
        return ResponseEntity.ok(updatedClaim);
    }
     
    //full details
    @GetMapping("/fullDetails/{agentId}")
    public ResponseEntity<AgentFullDetails> getAgentFullDetails(@PathVariable Integer agentId) {
        AgentFullDetails details = ser.getAgentFullDetails(agentId);
        return ResponseEntity.ok(details);
    }
    @PostMapping("/login")
    public String login(@RequestBody Agent loginAgent) {
//        Logger.info("Attempting to login user with email: {}", loginAgent.getContactInfo());
        return ser.loginAgent(loginAgent.getContactInfo(), loginAgent.getPassword());
    }
    
    @GetMapping("/getAgentDetails/{policyId}")
    public List<Agent> getAgent(@PathVariable("policyId") Integer policyId){
    	List<Agent> agents = ser.getallagentsbypolicyId(policyId);
    	return agents;
    }
    
    	
    


	
	

}
