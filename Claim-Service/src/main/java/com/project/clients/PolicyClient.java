package com.project.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.netflix.spectator.impl.Scheduler.Policy;

@FeignClient(name = "POLICY-SERVICE", path = "/api/policies")
public interface PolicyClient {
    @GetMapping("/api/policies/{id}")
    Policy getPolicyById(@PathVariable("policy_id") Integer policyId);
}

