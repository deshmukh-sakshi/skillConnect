package com.skillconnect.backend.Controller;

import com.skillconnect.backend.DTO.ApiResponse;
import com.skillconnect.backend.DTO.FreelancerDTO;
import com.skillconnect.backend.DTO.FreelancerUpdateDTO;
import com.skillconnect.backend.DTO.PastWorkDTO;
import com.skillconnect.backend.DTO.PastWorkUpdateDTO;
import com.skillconnect.backend.Service.freelancer.FreelancerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PastWorkControllerTest {

    @Mock
    private FreelancerService freelancerService;

    @InjectMocks
    private FreelancerController freelancerController;

    @Test
    void updateFreelancer_addPastWork_returnsOkEnvelope() {
        PastWorkUpdateDTO pwDto = new PastWorkUpdateDTO();
        pwDto.setTitle("Portfolio");
        pwDto.setLink("https://example.com");
        pwDto.setDescription("Client project");

        FreelancerUpdateDTO request = new FreelancerUpdateDTO();
        request.setName("Sam");
        request.setPastWorks(List.of(pwDto));

        PastWorkDTO savedWork = new PastWorkDTO();
        savedWork.setTitle("Portfolio");
        savedWork.setLink("https://example.com");

        FreelancerDTO updated = new FreelancerDTO();
        updated.setName("Sam");
        updated.setPastWorks(List.of(savedWork));

        when(freelancerService.updateFreelancerProfile(4L, request)).thenReturn(updated);

        ResponseEntity<ApiResponse<FreelancerDTO>> response = freelancerController.updateFreelancer(4L, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("success", response.getBody().getStatus());
        assertEquals(1, response.getBody().getData().getPastWorks().size());
        assertEquals("Portfolio", response.getBody().getData().getPastWorks().getFirst().getTitle());
    }

    @Test
    void updateFreelancer_deletePastWork_returnsEmptyPastWorks() {
        PastWorkUpdateDTO pwDto = new PastWorkUpdateDTO();
        pwDto.setId(9L);
        pwDto.setTitle("Old");
        pwDto.setLink("https://old");
        pwDto.setDescription("desc");
        pwDto.setToDelete(true);

        FreelancerUpdateDTO request = new FreelancerUpdateDTO();
        request.setName("Sam");
        request.setPastWorks(List.of(pwDto));

        FreelancerDTO updated = new FreelancerDTO();
        updated.setName("Sam");
        updated.setPastWorks(List.of());

        when(freelancerService.updateFreelancerProfile(4L, request)).thenReturn(updated);

        ResponseEntity<ApiResponse<FreelancerDTO>> response = freelancerController.updateFreelancer(4L, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().getData().getPastWorks().isEmpty());
    }

    @Test
    void updateFreelancer_whenServiceThrows_propagatesException() {
        FreelancerUpdateDTO request = new FreelancerUpdateDTO();
        request.setName("Sam");
        request.setPastWorks(List.of());

        when(freelancerService.updateFreelancerProfile(4L, request))
                .thenThrow(new RuntimeException("Freelancer not found"));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> freelancerController.updateFreelancer(4L, request));

        assertEquals("Freelancer not found", ex.getMessage());
    }
}
