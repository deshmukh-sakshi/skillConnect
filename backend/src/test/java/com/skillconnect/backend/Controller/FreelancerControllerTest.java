package com.skillconnect.backend.Controller;

import com.skillconnect.backend.DTO.ApiResponse;
import com.skillconnect.backend.DTO.FreelancerDTO;
import com.skillconnect.backend.Service.freelancer.FreelancerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FreelancerControllerTest {

    @Mock
    private FreelancerService freelancerService;

    @InjectMocks
    private FreelancerController freelancerController;

    @Test
    void getProfile_returnsSuccessEnvelope() {
        FreelancerDTO dto = new FreelancerDTO();
        dto.setName("Sam");

        when(freelancerService.getFreelancerProfile(4L)).thenReturn(dto);

        ResponseEntity<ApiResponse<FreelancerDTO>> response = freelancerController.getProfile(4L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("success", response.getBody().getStatus());
        assertEquals("Sam", response.getBody().getData().getName());
    }

    @Test
    void getProfile_whenServiceThrows_propagatesException() {
        when(freelancerService.getFreelancerProfile(4L)).thenThrow(new RuntimeException("Freelancer not found"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> freelancerController.getProfile(4L));

        assertEquals("Freelancer not found", ex.getMessage());
    }

    @Test
    void deleteFreelancer_returnsSuccessEnvelope() {
        doNothing().when(freelancerService).deleteFreelancer(4L);

        ResponseEntity<ApiResponse<String>> response = freelancerController.deleteFreelancer(4L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("success", response.getBody().getStatus());
        assertEquals("Freelancer deleted successfully.", response.getBody().getData());
    }

    @Test
    void deleteFreelancer_whenServiceThrows_propagatesException() {
        doThrow(new RuntimeException("Freelancer not found")).when(freelancerService).deleteFreelancer(4L);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> freelancerController.deleteFreelancer(4L));

        assertEquals("Freelancer not found", ex.getMessage());
    }
}

