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
	
	@GetMapping("get/{id}")
	public ResponseEntity<Agent> getAgentById(@PathVariable int id){
		return ser.getAgentById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
				
	}
	

	
//	@GetMapping("get/{id}")
//	public ResponseEntity<Agent> getAgentById(@PathVariable Long id){
//		return ser.getAgentById(id)
//				.map(ResponseEntity::ok)
//				.orElse(ResponseEntity.notFound().build());
//		
//		
//	}
//	
	
	
//	@GetMapping("/{id}")
//	public String getAgentById(@PathVariable Long id) {
//		return ser.getAgentById(id);
//	}
	
//	@PutMapping("/update/{id}")
//	public ResponseEntity<Agent> updateAgent(@PathVariable Long id,@RequestBody Agent agent){
//		try {
//			Agent updatedag = ser.updateAgent(id, agent);
//			return ResponseEntity.ok(updatedag);
//			
//		}
//		catch(Exception e) {
//			return ResponseEntity.notFound().build();
//		}
//	}
	
	//delete the data
//	@DeleteMapping("/delete/{id}")
//	public String deleteAgent(@PathVariable Long id) {
//		ser.deleteAgent(id);
//		return "Agent Deleted";
//		
//	}

}
