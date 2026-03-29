package com.skillconnect.backend.Controller;

import com.skillconnect.backend.DTO.ApiResponse;
import com.skillconnect.backend.DTO.FreelancerDTO;
import com.skillconnect.backend.DTO.FreelancerUpdateDTO;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SkillControllerTest {

    @Mock
    private FreelancerService freelancerService;

    @InjectMocks
    private FreelancerController freelancerController;

    @Test
    void updateFreelancer_withSkills_returnsOkEnvelope() {
        FreelancerUpdateDTO request = new FreelancerUpdateDTO();
        request.setName("Sam");
        request.setSkills(List.of("Java", "Spring"));

        FreelancerDTO updated = new FreelancerDTO();
        updated.setName("Sam");
        updated.setSkills(List.of("Java", "Spring"));

        when(freelancerService.updateFreelancerProfile(7L, request)).thenReturn(updated);

        ResponseEntity<ApiResponse<FreelancerDTO>> response = freelancerController.updateFreelancer(7L, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("success", response.getBody().getStatus());
        assertEquals(List.of("Java", "Spring"), response.getBody().getData().getSkills());
    }

    @Test
    void updateFreelancer_removeSkills_returnsEmptySkillList() {
        FreelancerUpdateDTO request = new FreelancerUpdateDTO();
        request.setName("Sam");
        request.setSkills(List.of());

        FreelancerDTO updated = new FreelancerDTO();
        updated.setName("Sam");
        updated.setSkills(List.of());

        when(freelancerService.updateFreelancerProfile(7L, request)).thenReturn(updated);

        ResponseEntity<ApiResponse<FreelancerDTO>> response = freelancerController.updateFreelancer(7L, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().getData().getSkills().isEmpty());
    }

    @Test
    void updateFreelancer_whenServiceThrows_propagatesException() {
        FreelancerUpdateDTO request = new FreelancerUpdateDTO();
        request.setName("Sam");
        request.setSkills(List.of("Rust"));

        when(freelancerService.updateFreelancerProfile(7L, request))
                .thenThrow(new RuntimeException("Freelancer not found"));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> freelancerController.updateFreelancer(7L, request));

        assertEquals("Freelancer not found", ex.getMessage());
    }
}
