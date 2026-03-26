package com.skillconnect.backend.DTO;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class BidResponseDTOTest {

    @Test
    void noArgsConstructor_createsInstance() {
        BidResponseDTO dto = new BidResponseDTO();
        
        assertNotNull(dto);
        assertNull(dto.getBidId());
        assertNull(dto.getFreelancerId());
        assertNull(dto.getProject());
        assertNull(dto.getProposal());
        assertNull(dto.getBidAmount());
        assertNull(dto.getDurationDays());
        assertNull(dto.getStatus());
        assertNull(dto.getCreatedAt());
    }

    @Test
    void allArgsConstructor_setsAllFields() {
        LocalDateTime now = LocalDateTime.now();
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setId(3L);
        BidResponseDTO dto = new BidResponseDTO(1L, 2L, projectDTO, null, "Proposal", 1500.0, 20L, null, "PENDING", now);
        
        assertEquals(1L, dto.getBidId());
        assertEquals(2L, dto.getFreelancerId());
        assertNotNull(dto.getProject());
        assertEquals(3L, dto.getProject().getId());
        assertEquals("Proposal", dto.getProposal());
        assertEquals(1500.0, dto.getBidAmount());
        assertEquals(20L, dto.getDurationDays());
        assertEquals("PENDING", dto.getStatus());
        assertEquals(now, dto.getCreatedAt());
    }

    @Test
    void settersAndGetters_workCorrectly() {
        BidResponseDTO dto = new BidResponseDTO();
        LocalDateTime timestamp = LocalDateTime.of(2024, 1, 15, 10, 30);
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setId(300L);
        
        dto.setBidId(100L);
        dto.setFreelancerId(200L);
        dto.setProject(projectDTO);
        dto.setProposal("Detailed proposal");
        dto.setBidAmount(3000.75);
        dto.setDurationDays(60L);
        dto.setStatus("ACCEPTED");
        dto.setCreatedAt(timestamp);
        
        assertEquals(100L, dto.getBidId());
        assertEquals(200L, dto.getFreelancerId());
        assertNotNull(dto.getProject());
        assertEquals(300L, dto.getProject().getId());
        assertEquals("Detailed proposal", dto.getProposal());
        assertEquals(3000.75, dto.getBidAmount());
        assertEquals(60L, dto.getDurationDays());
        assertEquals("ACCEPTED", dto.getStatus());
        assertEquals(timestamp, dto.getCreatedAt());
    }

    @Test
    void setStatus_withDifferentValues_setsCorrectly() {
        BidResponseDTO dto = new BidResponseDTO();
        
        dto.setStatus("PENDING");
        assertEquals("PENDING", dto.getStatus());
        
        dto.setStatus("ACCEPTED");
        assertEquals("ACCEPTED", dto.getStatus());
        
        dto.setStatus("REJECTED");
        assertEquals("REJECTED", dto.getStatus());
    }

    @Test
    void setCreatedAt_withPastDate_setsCorrectly() {
        BidResponseDTO dto = new BidResponseDTO();
        LocalDateTime pastDate = LocalDateTime.of(2023, 6, 1, 12, 0);
        
        dto.setCreatedAt(pastDate);
        
        assertEquals(pastDate, dto.getCreatedAt());
    }

    @Test
    void setNullValues_setsCorrectly() {
        LocalDateTime now = LocalDateTime.now();
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setId(3L);
        BidResponseDTO dto = new BidResponseDTO(1L, 2L, projectDTO, null, "Proposal", 1500.0, 20L, null, "PENDING", now);
        
        dto.setBidId(null);
        dto.setFreelancerId(null);
        dto.setProject(null);
        dto.setProposal(null);
        dto.setBidAmount(null);
        dto.setDurationDays(null);
        dto.setStatus(null);
        dto.setCreatedAt(null);
        
        assertNull(dto.getBidId());
        assertNull(dto.getFreelancerId());
        assertNull(dto.getProject());
        assertNull(dto.getProposal());
        assertNull(dto.getBidAmount());
        assertNull(dto.getDurationDays());
        assertNull(dto.getStatus());
        assertNull(dto.getCreatedAt());
    }
}
