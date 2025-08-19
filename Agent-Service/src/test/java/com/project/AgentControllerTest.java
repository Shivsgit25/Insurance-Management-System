package com.project;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.project.controller.AgentController;
import com.project.model.Agent;
import com.project.service.AgentService;

@WebMvcTest(AgentController.class)
class AgentControllerTest {
	    @Autowired
	    private MockMvc mockMvc;

	    @MockBean
	    private AgentService agentService;

	    @Test
	    void testCreateAgent() throws Exception {
	        Agent agent = new Agent(1, "contact", "John", 101, 201, 301,"pass123");
	        when(agentService.createAgent(any(Agent.class))).thenReturn("Agent saved");

	        String jsonPayload = """
	            {
	                "agentId": 1,
	                "contactInfo": "contact",
	                "name": "John",
	                "policyId": 101,
	                "claimId": 201,
	                "customerId": 301,
	                "password":"pass123"
	            }
	        """;

	        mockMvc.perform(post("/agents/save")
	                .contentType(MediaType.APPLICATION_JSON)
	                .content(jsonPayload))
	                .andExpect(status().isOk())
	                .andExpect(content().string("Agent saved"));

	        verify(agentService).createAgent(any(Agent.class));
	    }
	    
	    @Test
	    void testGetAllAgents() throws Exception {
	        List<Agent> agents = List.of(
	            new Agent(1, "contact1", "John", 101, 201, 301,"pass1"),
	            new Agent(2, "contact2", "Jane", 102, 202, 302,"pass2")
	        );
	        when(agentService.getAllAgents()).thenReturn(agents);

	        mockMvc.perform(get("/agents/all"))
	            .andExpect(status().isOk())
	            .andExpect(jsonPath("$.length()").value(2))
	            .andExpect(jsonPath("$[0].name").value("John"))
	            .andExpect(jsonPath("$[1].name").value("Jane"));

	        verify(agentService).getAllAgents();
	    }
	    
	    @Test
	    void testGetAgentById_Success() throws Exception {
	    	Agent agent = new Agent(1,"contact","John",101,102,103,"pass1");
	    	when(agentService.getAgentById(1)).thenReturn(Optional.of(agent));
	    	mockMvc.perform(get("/agents/get/1"))
	    	       .andExpect(status().isOk())
	    	       .andExpect(jsonPath("$.name").value("John"));
	    	
	    	verify(agentService).getAgentById(1);
	    	
	    }
	    
	    @Test
	    void testGtAgentById_NotFound() throws Exception {
	    	
	    	when(agentService.getAgentById(99)).thenReturn(Optional.empty());
	    	mockMvc.perform(get("/agents/get/99"))
	    	.andExpect(status().isNotFound());
	    	
	    	verify(agentService).getAgentById(99);
	    }
	    
	    @Test
	    void testUpdateAgent_Success() throws Exception {
	    	
	    	Agent updated = new Agent(1, "updatedContact","UpdatedName",111,211,311,"pass1");
	    	when(agentService.updateAgent(eq(1), any(Agent.class))).thenReturn(updated);
	    	
	    	String jsonPayload = """
	    	        {
	    	            "agentId": 1,
	    	            "contactInfo": "updatedContact",
	    	            "name": "UpdatedName",
	    	            "policyId": 111,
	    	            "claimId": 211,
	    	            "customerId": 311,
	    	            "password":"pass1"
	    	        }
	    	    """;
	    	
	    	mockMvc.perform(put("/agents/update/1")
	                .contentType(MediaType.APPLICATION_JSON)
	                .content(jsonPayload))
	            .andExpect(status().isOk())
	            .andExpect(jsonPath("$.name").value("UpdatedName"));

	        verify(agentService).updateAgent(eq(1), any(Agent.class));
	    	
	    	
	    	
	    	
	    	
	    }
	    
	    @Test
	    void testUpdateAgent_NotFound() throws Exception {
	    	
	    	when(agentService.updateAgent(eq(99), any(Agent.class)))
	    	   .thenThrow(new RuntimeException("Agent not found"));
	    	
	    
	    
	    String jsonPayload = """
	            {
	                "agentId": 99,
	                "contactInfo": "contact",
	                "name": "Ghost",
	                "policyId": 101,
	                "claimId": 201,
	                "customerId": 301,
	                "password":"Passghost"
	            }
	        """;
	      
	    mockMvc.perform(put("/agents/update/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPayload))
            .andExpect(status().isNotFound());
            
            verify(agentService).updateAgent(eq(99), any(Agent.class));
	        
	        
	    }
	    
	    @Test
	    void testDeleteAgent() throws Exception {
	        when(agentService.deleteAgent(1)).thenReturn("Agent Deleted");

	        mockMvc.perform(delete("/agents/delete/1"))
	            .andExpect(status().isOk())
	            .andExpect(content().string("Agent Deleted"));

	        verify(agentService).deleteAgent(1);
	    }
	    
	    @Test
	    void testGetAgentByPolicy() throws Exception {
	        List<Agent> agents = List.of(
	            new Agent(1, "contact", "John", 101, 201, 301,"pass1")
	        );
	        when(agentService.getAgentByPolicy(101)).thenReturn(agents);

	        mockMvc.perform(get("/agents/policy/101"))
	            .andExpect(status().isOk())
	            .andExpect(jsonPath("$.length()").value(1))
	            .andExpect(jsonPath("$[0].policyId").value(101));

	        verify(agentService).getAgentByPolicy(101);
	    }




}
