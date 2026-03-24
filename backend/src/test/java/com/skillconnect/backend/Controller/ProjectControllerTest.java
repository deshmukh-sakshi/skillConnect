package com.skillconnect.backend.Controller;

import com.skillconnect.backend.DTO.ProjectDTO;
import com.skillconnect.backend.Service.project.ProjectService;
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
class ProjectControllerTest {

    @Mock
    private ProjectService projectService;

    @InjectMocks
    private ProjectController projectController;

    @Test
    void createProject_returnsCreatedWithBody() {
        ProjectDTO request = new ProjectDTO();
        ProjectDTO created = new ProjectDTO();
        created.setId(12L);

        when(projectService.createProject(request)).thenReturn(created);

        ResponseEntity<ProjectDTO> response = projectController.createProject(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(12L, response.getBody().getId());
    }

    @Test
    void getAllProjects_returnsList() {
        ProjectDTO dto = new ProjectDTO();
        dto.setId(1L);
        when(projectService.getAllProjects()).thenReturn(List.of(dto));

        List<ProjectDTO> response = projectController.getAllProjects();

        assertEquals(1, response.size());
        assertEquals(1L, response.getFirst().getId());
    }

    @Test
    void getProjectById_whenFound_returnsOk() {
        ProjectDTO dto = new ProjectDTO();
        dto.setId(8L);
        when(projectService.getProjectById(8L)).thenReturn(dto);

        ResponseEntity<ProjectDTO> response = projectController.getProjectById(8L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(8L, response.getBody().getId());
    }

    @Test
    void getProjectById_whenMissing_returnsNotFound() {
        when(projectService.getProjectById(9L)).thenThrow(new RuntimeException("Project not found"));

        ResponseEntity<ProjectDTO> response = projectController.getProjectById(9L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void deleteProject_returnsExpectedMessages() {
        when(projectService.deleteProjectById(7L)).thenReturn(true);
        when(projectService.deleteProjectById(99L)).thenReturn(false);

        ResponseEntity<String> okResponse = projectController.deleteProject(7L);
        ResponseEntity<String> missingResponse = projectController.deleteProject(99L);

        assertEquals(HttpStatus.OK, okResponse.getStatusCode());
        assertEquals("Project deleted successfully", okResponse.getBody());
        assertEquals(HttpStatus.NOT_FOUND, missingResponse.getStatusCode());
        assertEquals("Project not found", missingResponse.getBody());
    }
}

