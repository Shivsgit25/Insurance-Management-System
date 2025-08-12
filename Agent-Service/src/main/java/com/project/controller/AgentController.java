package com.project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.model.Agent;
import com.project.service.AgentService;
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
	public ResponseEntity<Agent> getAgentById(@PathVariable int agentId){
		return ser.getAgentById(agentId)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
				
	}
	
    @PutMapping("/update/{agentId}")
	public ResponseEntity<Agent> updateAgent(@PathVariable int agentId, @RequestBody Agent agent){
		try {
		Agent updated = ser.updateAgent(agentId,agent);
		return ResponseEntity.ok(updated);
		} catch(Exception e) {
			return ResponseEntity.notFound().build();
		}
		
	}
    
    @DeleteMapping("/delete/{agentId}")
    public String deleteAgent(@PathVariable int agentId) {
    	ser.deleteAgent(agentId);
    	return "Agent Deleted";
    }
	

	
	

}
