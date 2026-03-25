package com.skillconnect.backend.Controller;

import com.skillconnect.backend.DTO.ApiResponse;
import com.skillconnect.backend.DTO.SkillDTO;
import com.skillconnect.backend.Service.Skills.SkillsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SkillControllerTest {

    @Mock
    private SkillsService skillService;

    @InjectMocks
    private SkillController skillController;

    @Test
    void addSkill_returnsCreatedEnvelope() {
        SkillDTO request = new SkillDTO(null, "Java", 7L);
        SkillDTO saved = new SkillDTO(11L, "Java", 7L);

        when(skillService.addSkill(request)).thenReturn(saved);

        ResponseEntity<ApiResponse<SkillDTO>> response = skillController.addSkill(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("success", response.getBody().getStatus());
        assertNotNull(response.getBody().getData());
        assertEquals(11L, response.getBody().getData().getId());
        assertEquals("Java", response.getBody().getData().getSkillName());
    }

    @Test
    void removeSkill_returnsSuccessEnvelope() {
        doNothing().when(skillService).removeSkillFromFreelancer(7L, "Java");

        ResponseEntity<ApiResponse<String>> response = skillController.removeSkill(7L, "Java");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("success", response.getBody().getStatus());
        assertEquals("Skill removed", response.getBody().getData());
    }

    @Test
    void removeSkill_whenServiceThrows_propagatesException() {
        doThrow(new RuntimeException("Skill not found"))
                .when(skillService).removeSkillFromFreelancer(7L, "Rust");

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> skillController.removeSkill(7L, "Rust"));

        assertEquals("Skill not found", ex.getMessage());
    }
}

