package com.skillconnect.backend.DTO;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BidDTOTest {

    @Test
    void noArgsConstructor_createsInstance() {
        BidDTO bidDTO = new BidDTO();
        
        assertNotNull(bidDTO);
        assertNull(bidDTO.getFreelancerId());
        assertNull(bidDTO.getProjectId());
        assertNull(bidDTO.getProposal());
        assertNull(bidDTO.getBidAmount());
        assertNull(bidDTO.getDurationDays());
    }

    @Test
    void allArgsConstructor_setsAllFields() {
        BidDTO bidDTO = new BidDTO(1L, 2L, "Test proposal", 1000.0, 30L, null);
        
        assertEquals(1L, bidDTO.getFreelancerId());
        assertEquals(2L, bidDTO.getProjectId());
        assertEquals("Test proposal", bidDTO.getProposal());
        assertEquals(1000.0, bidDTO.getBidAmount());
        assertEquals(30L, bidDTO.getDurationDays());
    }

    @Test
    void settersAndGetters_workCorrectly() {
        BidDTO bidDTO = new BidDTO();
        
        bidDTO.setFreelancerId(10L);
        bidDTO.setProjectId(20L);
        bidDTO.setProposal("My proposal");
        bidDTO.setBidAmount(2500.50);
        bidDTO.setDurationDays(45L);
        
        assertEquals(10L, bidDTO.getFreelancerId());
        assertEquals(20L, bidDTO.getProjectId());
        assertEquals("My proposal", bidDTO.getProposal());
        assertEquals(2500.50, bidDTO.getBidAmount());
        assertEquals(45L, bidDTO.getDurationDays());
    }

    @Test
    void setProposal_withLongText_setsCorrectly() {
        BidDTO bidDTO = new BidDTO();
        String longProposal = "This is a very long proposal text that describes the freelancer's approach to the project in detail.";
        
        bidDTO.setProposal(longProposal);
        
        assertEquals(longProposal, bidDTO.getProposal());
    }

    @Test
    void setBidAmount_withDecimalValue_setsCorrectly() {
        BidDTO bidDTO = new BidDTO();
        
        bidDTO.setBidAmount(1234.56);
        
        assertEquals(1234.56, bidDTO.getBidAmount());
    }

    @Test
    void setDurationDays_withZero_setsCorrectly() {
        BidDTO bidDTO = new BidDTO();
        
        bidDTO.setDurationDays(0L);
        
        assertEquals(0L, bidDTO.getDurationDays());
    }

    @Test
    void setNullValues_setsCorrectly() {
        BidDTO bidDTO = new BidDTO(1L, 2L, "Proposal", 1000.0, 30L, null);
        
        bidDTO.setFreelancerId(null);
        bidDTO.setProjectId(null);
        bidDTO.setProposal(null);
        bidDTO.setBidAmount(null);
        bidDTO.setDurationDays(null);
        
        assertNull(bidDTO.getFreelancerId());
        assertNull(bidDTO.getProjectId());
        assertNull(bidDTO.getProposal());
        assertNull(bidDTO.getBidAmount());
        assertNull(bidDTO.getDurationDays());
    }
}
