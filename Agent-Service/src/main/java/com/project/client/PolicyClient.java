package com.project.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.project.model.PolicyDTO;

@FeignClient(name = "POLICY-SERVICE", path = "/api/policies")
public interface PolicyClient {

    @GetMapping("/testing/{agentId}")
    public List<PolicyDTO> getPolicies(@PathVariable("agentId") Integer agentId);

    @GetMapping("/{policyId}")
    PolicyDTO getPolicyById(@PathVariable Integer policyId);

}
