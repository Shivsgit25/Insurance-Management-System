package com.project.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.client.CustomerClient;
import com.project.client.PolicyClient;
import com.project.model.Agent;
import com.project.model.AgentCustomer;
import com.project.model.AgentPolicy;
import com.project.model.CustomerDTO;
import com.project.model.PolicyDTO;
import com.project.repository.AgentRepository;
//import com.project.repository.AgentRepository;


@Service
public class AgentServiceImpl implements AgentService {
	
	@Autowired
	private AgentRepository repo;
	
	@Autowired
	PolicyClient policyclient;
	
	@Autowired
	CustomerClient customerclient; 

	@Override
	public String createAgent(Agent agent) {
	    repo.save(agent);
	    return "Agent saved";
	    
	}

	@Override
	public List<Agent> getAllAgents() {
		return repo.findAll();
	
	}
	
    @Override
    public Optional<Agent> getAgentById(Integer agentId){
    	return repo.findById(agentId);
    	
    }

	@Override
	public Agent updateAgent(Integer agentId, Agent updateAgent) {
		return repo.findById(agentId)
				.map(agent -> {
					agent.setName(updateAgent.getName());
					agent.setContactInfo(updateAgent.getContactInfo());
					agent.setAgentId(updateAgent.getAgentId());
					agent.setPolicyId(updateAgent.getPolicyId());
					agent.setClaimId(updateAgent.getClaimId());
					agent.setCustomerId(updateAgent.getCustomerId());
					
					
					return repo.save(agent);
				})
				.orElseThrow(()-> new RuntimeException("Agent not found"));
	
	}

	@Override
	public String deleteAgent(Integer agentId) {
		
	      repo.deleteById(agentId);
	      return "Agent Deleted";
	}
	
	
	
	
	public List<Agent> getAgentByPolicy(Integer policyId){
		return repo.findByPolicyId(policyId);
	}

	

	@Override
	public AgentPolicy getAgentPolyCombo(Integer aid) {
	  List<PolicyDTO> policydto = policyclient.getPolicies(aid);
	  Optional<Agent> opt = repo.findById(aid);
	  Agent agent = opt.get();
	  AgentPolicy agentpolicy = new AgentPolicy();
	  agentpolicy.setAgent(agent);
	  agentpolicy.setPolicy(policydto);
	  
	 return agentpolicy;
	}

	@Override
	public AgentCustomer getAgentCustCombo(Integer aid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AgentCustomer getCustomerForAgent(Integer aid) {
	
		List<CustomerDTO> custdto = customerclient.getCustomerForAgent(aid);
		Optional<Agent> opt = repo.findByCustomerId(aid);
		Agent agent = opt.get();
		AgentCustomer agentcustomer = new AgentCustomer();
		agentcustomer.setAgent(agent);
		agentcustomer.setCust(custdto);
		return agentcustomer;
	}

//	@Override
//	public AgentCustomer getAgentCustCombo(Integer aid) {
//		
//		List<CustomerDTO> custdto = customerclient.getCustomers(aid);
//		Optional<Agent> opt = repo.findByCustomerId(aid);
//		Agent agent = opt.get();
//		AgentCustomer agentcustomer = new AgentCustomer();
//		agentcustomer.setAgent(agent);
//		agentcustomer.setCust(custdto);
//		
//		return agentcustomer;
//	}



	

	
	


}
