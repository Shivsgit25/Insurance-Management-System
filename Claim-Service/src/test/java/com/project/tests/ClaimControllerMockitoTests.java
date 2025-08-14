package com.project.tests;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.controller.ClaimController;
import com.project.model.Claim;
import com.project.model.Claim.Status;
import com.project.service.ClaimService;

@WebMvcTest(ClaimController.class)
class ClaimControllerMockMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClaimService claimService;

    @Autowired
    private ObjectMapper objectMapper;

    private Claim sampleClaim;

    @BeforeEach
    void setUp() {
        sampleClaim = new Claim();
        sampleClaim.setClaimId(1);
        sampleClaim.setPolicyId(101);
        sampleClaim.setCustomerId(201);
        sampleClaim.setAgentId(301);
        sampleClaim.setClaimAmount(7500.0);
        sampleClaim.setStatus(Status.FILED);
    }

    @Test
    void testFileClaim() throws Exception {
        when(claimService.fileClaim(any())).thenReturn(sampleClaim);

        mockMvc.perform(post("/api/claims/file")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleClaim)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.claimId").value(1));
    }

    @Test
    void testGetClaimById_Found() throws Exception {
        when(claimService.getClaimById(1)).thenReturn(sampleClaim);

        mockMvc.perform(get("/api/claims/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.claimId").value(1));
    }

    @Test
    void testGetClaimById_NotFound() throws Exception {
        when(claimService.getClaimById(99)).thenReturn(null);

        mockMvc.perform(get("/api/claims/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateClaimStatus() throws Exception {
        sampleClaim.setStatus(Status.APPROVED);
        when(claimService.reviewClaim(1, Status.APPROVED)).thenReturn(sampleClaim);

        mockMvc.perform(put("/api/claims/1/status")
                .param("status", "APPROVED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("APPROVED"));
    }

    @Test
    void testGetClaimsByCustomer() throws Exception {
        when(claimService.getClaimsByCustomer(201)).thenReturn(List.of(sampleClaim));

        mockMvc.perform(get("/api/claims/customer/201"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].customerId").value(201));
    }

    @Test
    void testGetAllClaims() throws Exception {
        when(claimService.getAllClaims()).thenReturn(List.of(sampleClaim));

        mockMvc.perform(get("/api/claims/claims/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].claimId").value(1));
    }

    @Test
    void testGetClaimsByStatus() throws Exception {
        when(claimService.getClaimsByStatus(Status.FILED)).thenReturn(List.of(sampleClaim));

        mockMvc.perform(get("/api/claims/status/filed"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("FILED"));
    }
}
