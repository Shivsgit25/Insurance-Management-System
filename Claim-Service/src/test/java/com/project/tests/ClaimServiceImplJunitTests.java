package com.project.tests;

import com.project.model.Claim;
import com.project.model.Claim.Status;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class ClaimServiceImplJUnitTest {

    @Test
    void testClaimStatusAssignment() {
        Claim claim = new Claim();
        claim.setStatus(Status.FILED);
        assertEquals(Status.FILED, claim.getStatus());
    }

    @Test
    void testClaimFieldSettersAndGetters() {
        Claim claim = new Claim();
        claim.setClaimId(1);
        claim.setPolicyId(101);
        claim.setCustomerId(201);
        claim.setAgentId(301);
        claim.setClaimAmount(7500.0);

        assertEquals(1, claim.getClaimId());
        assertEquals(101, claim.getPolicyId());
        assertEquals(201, claim.getCustomerId());
        assertEquals(301, claim.getAgentId());
        assertEquals(7500.0, claim.getClaimAmount());
    }
}
