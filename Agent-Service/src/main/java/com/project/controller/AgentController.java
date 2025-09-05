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


@RestController
@RequestMapping("/agents")
public class AgentController {
	
	@Autowired
	private AgentService ser;
	
	/**
	 * Creates a new agent and persists it to the database.
	 *
	 * @param agent The Agent object containing details to be saved.
	 * @return A confirmation message indicating successful creation.
	 */

	@PostMapping("/save")
	public String createAgent(@RequestBody Agent agent) {
	    return ser.createAgent(agent);
	}
	/**
	 * Retrieves all agents currently stored in the system.
	 *
	 * @return A ResponseEntity containing a list of Agent objects.
	 */
	
	@GetMapping("/all")
	public ResponseEntity<List<Agent>> getAllAgents(){
		return ResponseEntity.ok(ser.getAllAgents());
	}
	/**
	 * Fetches a specific agent by their unique ID.
	 *
	 * @param agentId The ID of the agent to retrieve.
	 * @return A ResponseEntity with the Agent object if found, or 404 if not.
	 */
	
	@GetMapping("get/{agentId}")
	public ResponseEntity<Agent> getAgentById(@PathVariable Integer agentId){
		return ser.getAgentById(agentId)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
				
	}
	
	/**
	 * Updates an existing agent's details based on the provided agent ID.
	 *
	 * @param agentId The ID of the agent to update.
	 * @param agent The updated Agent object containing new details.
	 * @return A ResponseEntity with the updated Agent object, or 404 if the agent is not found.
	 */
	
    @PutMapping("/update/{agentId}")
	public ResponseEntity<Agent> updateAgent(@PathVariable Integer agentId, @RequestBody Agent agent){
		try {
		Agent updated = ser.updateAgent(agentId,agent);
		return ResponseEntity.ok(updated);
		} catch(Exception e) {
			return ResponseEntity.notFound().build();
		}
		
	}
    /**
     * Deletes an agent from the system based on the provided agent ID.
     *
     * @param agentId The ID of the agent to delete.
     * @return A confirmation message indicating successful deletion.
     */
    
    @DeleteMapping("/delete/{agentId}")
    public String deleteAgent(@PathVariable Integer agentId) {
    	ser.deleteAgent(agentId);
    	return "Agent Deleted";
    }
    /**
     * Retrieves all agents associated with a given policy ID.
     *
     * @param policyId The ID of the policy to filter agents by.
     * @return A ResponseEntity containing a list of matching Agent objects.
     */
	
    
    //get agent by policyId
    @GetMapping("/policy/{policyId}")
    public ResponseEntity<List<Agent>> getAgentByPolicy(@PathVariable Integer policyId){
    	List<Agent> agents = ser.getAgentByPolicy(policyId);
    	return ResponseEntity.ok(agents);
    }
    
    /**
     * Returns combined agent and policy details for a specific agent.
     *
     * @param aid The ID of the agent.
     * @return An AgentPolicy object containing agent and policy information.
     */
    
    
    @GetMapping("/getAgentPolicyDetails/{aid}")
	public AgentPolicy test(@PathVariable("aid") Integer aid) {
		
    	return ser.getAgentPolyCombo(aid);
	}
    
    /**
     * Retrieves customer details linked to a specific agent.
     *
     * @param cid The customer ID associated with the agent.
     * @return A CustomerDTO object containing customer information.
     */
    
    @GetMapping("/getCustomerForAgent/{cid}")
	public CustomerDTO getCustomerForAgent(@PathVariable("cid") Integer cid) {
		
    	return ser.getCustomerForAgent(cid);
	}
    
    /**
     * Fetches all claims handled by agents.
     *
     * @return A ResponseEntity containing an AgentClaim object with claim data.
     */
    

    
    @GetMapping("/claims/all")
	public ResponseEntity<AgentClaim> getAllClaims(){
    	return ResponseEntity.ok(ser.getAllClaims());
    }
    /**
     * Approves a claim associated with a specific claim ID.
     *
     * @param claimId The ID of the claim to approve.
     * @return A ResponseEntity containing the updated ClaimDTO object.
     */
    
    
    @PutMapping("/approve-claim/{claimId}")
    public ResponseEntity<ClaimDTO> approveClaim(@PathVariable Integer claimId) {
        ClaimDTO updatedClaim = ser.approveClaim(claimId);
        return ResponseEntity.ok(updatedClaim);
    }
    /**
     * Provides full details of an agent including linked policies, claims, and customer info.
     *
     * @param agentId The ID of the agent to retrieve full details for.
     * @return A ResponseEntity containing an AgentFullDetails object.
     */
     
    //full details
    @GetMapping("/fullDetails/{agentId}")
    public ResponseEntity<AgentFullDetails> getAgentFullDetails(@PathVariable Integer agentId) {
        AgentFullDetails details = ser.getAgentFullDetails(agentId);
        return ResponseEntity.ok(details);
    }
    /**
     * Authenticates an agent using contact information and password.
     *
     * @param loginAgent The Agent object containing login credentials.
     * @return A login status message or token.
     */
    @PostMapping("/login")
    public String login(@RequestBody Agent loginAgent) {

        return ser.loginAgent(loginAgent.getContactInfo(), loginAgent.getPassword());
    }
    
    /**
     * Retrieves all agents linked to a specific policy ID.
     *
     * @param policyId The ID of the policy to search agents by.
     * @return A list of Agent objects associated with the given policy.
     */
    
    @GetMapping("/getAgentDetails/{policyId}")
    public List<Agent> getAgent(@PathVariable("policyId") Integer policyId){
    	List<Agent> agents = ser.getallagentsbypolicyId(policyId);
    	return agents;
    }
    
    	
    


	
	

}
