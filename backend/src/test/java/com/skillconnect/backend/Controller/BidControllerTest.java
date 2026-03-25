package com.skillconnect.backend.Controller;

import com.skillconnect.backend.DTO.ApiResponse;
import com.skillconnect.backend.DTO.BidDTO;
import com.skillconnect.backend.DTO.BidResponseDTO;
import com.skillconnect.backend.Entity.Bids;
import com.skillconnect.backend.Service.bid.BidService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BidControllerTest {

    @Mock
    private BidService bidService;

    @InjectMocks
    private BidController bidController;

    @Test
    void placeBid_returnsCreatedEnvelope() {
        BidDTO dto = new BidDTO(2L, 3L, "I can do this", 5000.0, 20L, null);
        Bids bid = new Bids();
        bid.setId(11L);

        when(bidService.placeBid(dto)).thenReturn(bid);

        ResponseEntity<ApiResponse<Bids>> response = bidController.placeBid(dto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("success", response.getBody().getStatus());
        assertEquals(11L, response.getBody().getData().getId());
    }

    @Test
    void getBidsByFreelancer_whenBidsExist_returnsSuccessEnvelope() {
        BidResponseDTO bid = new BidResponseDTO();
        bid.setBidId(8L);
        when(bidService.getBidsByFreelancerId(4L)).thenReturn(List.of(bid));

        ResponseEntity<ApiResponse<List<BidResponseDTO>>> response = bidController.getBidsByFreelancer(4L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("success", response.getBody().getStatus());
        assertEquals(1, response.getBody().getData().size());
        assertEquals(8L, response.getBody().getData().getFirst().getBidId());
    }

    @Test
    void getBidsByFreelancer_whenEmpty_returnsErrorEnvelope() {
        when(bidService.getBidsByFreelancerId(4L)).thenReturn(List.of());

        ResponseEntity<ApiResponse<List<BidResponseDTO>>> response = bidController.getBidsByFreelancer(4L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("error", response.getBody().getStatus());
        assertEquals("No bids raised by this freelancer.", response.getBody().getError());
    }

    @Test
    void deleteBid_whenServiceSucceeds_returnsSuccessEnvelope() {
        doNothing().when(bidService).deleteBid(10L, 3L);

        ResponseEntity<ApiResponse<String>> response = bidController.deleteBid(10L, 3L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("success", response.getBody().getStatus());
        assertEquals("Bid deleted successfully.", response.getBody().getData());
    }

    @Test
    void deleteBid_whenServiceThrows_returnsErrorEnvelope() {
        doThrow(new RuntimeException("Only pending bids can be deleted."))
                .when(bidService).deleteBid(10L, 3L);

        ResponseEntity<ApiResponse<String>> response = bidController.deleteBid(10L, 3L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("error", response.getBody().getStatus());
        assertEquals("Only pending bids can be deleted.", response.getBody().getError());
    }
}

