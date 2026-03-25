package com.skillconnect.backend.DTO;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ContractResponseTest {

    @Test
    void builder_createsInstanceWithAllFields() {
        LocalDateTime createdAt = LocalDateTime.of(2024, 1, 1, 10, 0);
        LocalDateTime updatedAt = LocalDateTime.of(2024, 1, 2, 15, 30);
        
        ContractResponse response = ContractResponse.builder()
                .contractId(1L)
                .projectId(2L)
                .bidId(3L)
                .contractStatus("ACTIVE")
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
        
        assertNotNull(response);
        assertEquals(1L, response.getContractId());
        assertEquals(2L, response.getProjectId());
        assertEquals(3L, response.getBidId());
        assertEquals("ACTIVE", response.getContractStatus());
        assertEquals(createdAt, response.getCreatedAt());
        assertEquals(updatedAt, response.getUpdatedAt());
    }

    @Test
    void builder_createsInstanceWithNullValues() {
        ContractResponse response = ContractResponse.builder()
                .contractId(null)
                .projectId(null)
                .bidId(null)
                .contractStatus(null)
                .createdAt(null)
                .updatedAt(null)
                .build();
        
        assertNotNull(response);
        assertNull(response.getContractId());
        assertNull(response.getProjectId());
        assertNull(response.getBidId());
        assertNull(response.getContractStatus());
        assertNull(response.getCreatedAt());
        assertNull(response.getUpdatedAt());
    }

    @Test
    void builder_createsActiveContract() {
        LocalDateTime now = LocalDateTime.now();
        
        ContractResponse response = ContractResponse.builder()
                .contractId(10L)
                .projectId(20L)
                .bidId(30L)
                .contractStatus("ACTIVE")
                .createdAt(now)
                .updatedAt(now)
                .build();
        
        assertEquals("ACTIVE", response.getContractStatus());
        assertEquals(10L, response.getContractId());
    }

    @Test
    void builder_createsCompletedContract() {
        LocalDateTime createdAt = LocalDateTime.of(2024, 1, 1, 10, 0);
        LocalDateTime updatedAt = LocalDateTime.of(2024, 2, 1, 10, 0);
        
        ContractResponse response = ContractResponse.builder()
                .contractId(5L)
                .projectId(15L)
                .bidId(25L)
                .contractStatus("COMPLETED")
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
        
        assertEquals("COMPLETED", response.getContractStatus());
        assertTrue(updatedAt.isAfter(createdAt));
    }

    @Test
    void builder_createsCancelledContract() {
        ContractResponse response = ContractResponse.builder()
                .contractId(7L)
                .projectId(17L)
                .bidId(27L)
                .contractStatus("CANCELLED")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        
        assertEquals("CANCELLED", response.getContractStatus());
    }

    @Test
    void getters_returnCorrectValues() {
        LocalDateTime createdAt = LocalDateTime.of(2024, 3, 15, 9, 0);
        LocalDateTime updatedAt = LocalDateTime.of(2024, 3, 16, 14, 0);
        
        ContractResponse response = ContractResponse.builder()
                .contractId(100L)
                .projectId(200L)
                .bidId(300L)
                .contractStatus("PENDING")
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
        
        assertEquals(100L, response.getContractId());
        assertEquals(200L, response.getProjectId());
        assertEquals(300L, response.getBidId());
        assertEquals("PENDING", response.getContractStatus());
        assertEquals(createdAt, response.getCreatedAt());
        assertEquals(updatedAt, response.getUpdatedAt());
    }

    @Test
    void builder_withSameCreatedAndUpdatedTime_buildsCorrectly() {
        LocalDateTime timestamp = LocalDateTime.of(2024, 5, 10, 12, 0);
        
        ContractResponse response = ContractResponse.builder()
                .contractId(50L)
                .projectId(60L)
                .bidId(70L)
                .contractStatus("NEW")
                .createdAt(timestamp)
                .updatedAt(timestamp)
                .build();
        
        assertEquals(timestamp, response.getCreatedAt());
        assertEquals(timestamp, response.getUpdatedAt());
    }
}
