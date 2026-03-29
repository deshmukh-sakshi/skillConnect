package com.skillconnect.backend.Controller;

import com.skillconnect.backend.DTO.ApiResponse;
import com.skillconnect.backend.DTO.BidResponseDTO;
import com.skillconnect.backend.DTO.ClientDTO;
import com.skillconnect.backend.DTO.ProjectCountResponse;
import com.skillconnect.backend.DTO.ProjectCountsResponse;
import com.skillconnect.backend.DTO.ProjectDTO;
import com.skillconnect.backend.Service.project.ProjectService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProjectControllerTest {

    @Mock
    private ProjectService projectService;

    @InjectMocks
    private ProjectController projectController;

    @Test
    void createProject_returnsCreatedWithBody() {
        ClientDTO clientDTO = new ClientDTO("Client", "client@test.com", null);
        ProjectDTO request = new ProjectDTO(
                null,
                "Landing page",
                "Build landing page",
                "Web",
                LocalDateTime.of(2026, 5, 1, 12, 0),
                10000L,
                null,
                clientDTO,
                2L
        );
        ProjectDTO created = new ProjectDTO(
                12L,
                "Landing page",
                "Build landing page",
                "Web",
                LocalDateTime.of(2026, 5, 1, 12, 0),
                10000L,
                null,
                clientDTO,
                2L
        );

        when(projectService.createProject(request)).thenReturn(created);

        ResponseEntity<ApiResponse<ProjectDTO>> response = projectController.createProject(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("success", response.getBody().getStatus());
        assertNotNull(response.getBody().getData());
        assertEquals(12L, response.getBody().getData().getId());
    }

    @Test
    void getAllProjects_returnsList() {
        ProjectDTO dto = new ProjectDTO();
        dto.setId(1L);
        when(projectService.getAllProjects(null, "createdAt", "desc")).thenReturn(List.of(dto));

        ResponseEntity<ApiResponse<List<ProjectDTO>>> response = projectController.getAllProjects(null, "createdAt", "desc");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("success", response.getBody().getStatus());
        assertNotNull(response.getBody().getData());
        assertEquals(1, response.getBody().getData().size());
        assertEquals(1L, response.getBody().getData().getFirst().getId());
    }

    @Test
    void getAllProjects_withSortParams_returnsSortedList() {
        ProjectDTO dto = new ProjectDTO();
        dto.setId(2L);
        when(projectService.getAllProjects("web", "budget", "asc")).thenReturn(List.of(dto));

        ResponseEntity<ApiResponse<List<ProjectDTO>>> response = projectController.getAllProjects("web", "budget", "asc");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("success", response.getBody().getStatus());
        assertEquals(1, response.getBody().getData().size());
        assertEquals(2L, response.getBody().getData().getFirst().getId());
    }

    @Test
    void getProjectCountsByCategory_returnsOk() {
        ProjectCountsResponse countsResponse = new ProjectCountsResponse(
                List.of(new ProjectCountResponse("Web Development", 1L, 5L, null)),
                5L
        );
        when(projectService.getProjectCountsByCategory()).thenReturn(countsResponse);

        ResponseEntity<ApiResponse<ProjectCountsResponse>> response = projectController.getProjectCountsByCategory();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("success", response.getBody().getStatus());
        assertNotNull(response.getBody().getData());
        assertEquals(5L, response.getBody().getData().getTotalActiveProjects());
        assertEquals(1, response.getBody().getData().getCounts().size());
        assertEquals("Web Development", response.getBody().getData().getCounts().getFirst().getCategory());
    }

    @Test
    void getProjectById_whenFound_returnsOk() {
        ProjectDTO dto = new ProjectDTO();
        dto.setId(8L);
        when(projectService.getProjectById(8L)).thenReturn(dto);

        ResponseEntity<ApiResponse<ProjectDTO>> response = projectController.getProjectById(8L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("success", response.getBody().getStatus());
        assertNotNull(response.getBody().getData());
        assertEquals(8L, response.getBody().getData().getId());
    }

    @Test
    void getProjectById_whenMissing_returnsNotFound() {
        when(projectService.getProjectById(9L)).thenThrow(new RuntimeException("Project not found"));

        ResponseEntity<ApiResponse<ProjectDTO>> response = projectController.getProjectById(9L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("error", response.getBody().getStatus());
        assertEquals("Project not found", response.getBody().getError());
    }

    @Test
    void deleteProject_returnsExpectedMessages() {
        when(projectService.deleteProjectById(7L)).thenReturn(true);
        when(projectService.deleteProjectById(99L)).thenReturn(false);

        ResponseEntity<ApiResponse<String>> okResponse = projectController.deleteProject(7L);
        ResponseEntity<ApiResponse<String>> missingResponse = projectController.deleteProject(99L);

        assertEquals(HttpStatus.OK, okResponse.getStatusCode());
        assertNotNull(okResponse.getBody());
        assertEquals("success", okResponse.getBody().getStatus());
        assertEquals("Project deleted", okResponse.getBody().getData());

        assertEquals(HttpStatus.NOT_FOUND, missingResponse.getStatusCode());
        assertNotNull(missingResponse.getBody());
        assertEquals("error", missingResponse.getBody().getStatus());
        assertEquals("Project not found", missingResponse.getBody().getError());
    }

    @Test
    void getBidsByProject_returnsSuccessResponse() {
        BidResponseDTO bid = new BidResponseDTO();
        bid.setBidId(1L);
        when(projectService.getBidsByProjectId(4L)).thenReturn(List.of(bid));

        ResponseEntity<ApiResponse<List<BidResponseDTO>>> response = projectController.getBidsByProject(4L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("success", response.getBody().getStatus());
        assertNotNull(response.getBody().getData());
        assertEquals(1, response.getBody().getData().size());
        assertEquals(1L, response.getBody().getData().getFirst().getBidId());
    }

    @Test
    void updateProject_returnsUpdatedProject() {
        ProjectDTO request = new ProjectDTO();
        ProjectDTO updated = new ProjectDTO();
        updated.setId(10L);

        when(projectService.updateProject(10L, request)).thenReturn(updated);

        ResponseEntity<ApiResponse<ProjectDTO>> response = projectController.updateProject(10L, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("success", response.getBody().getStatus());
        assertNotNull(response.getBody().getData());
        assertEquals(10L, response.getBody().getData().getId());
    }

    @Test
    void updateProject_whenServiceThrows_propagatesException() {
        ProjectDTO request = new ProjectDTO();
        when(projectService.updateProject(10L, request)).thenThrow(new RuntimeException("Project not found"));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> projectController.updateProject(10L, request));

        assertEquals("Project not found", ex.getMessage());
    }

    @Test
    void acceptBid_returnsSuccessMessage() {
        doNothing().when(projectService).acceptBid(3L, 6L);

        ResponseEntity<ApiResponse<String>> response = projectController.acceptBid(3L, 6L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("success", response.getBody().getStatus());
        assertEquals("Project bid accepted", response.getBody().getData());
    }

    @Test
    void acceptBid_whenServiceThrows_propagatesException() {
        doThrow(new RuntimeException("Bid not found")).when(projectService).acceptBid(3L, 6L);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> projectController.acceptBid(3L, 6L));

        assertEquals("Bid not found", ex.getMessage());
    }

    @Test
    void rejectBid_returnsSuccessMessage() {
        doNothing().when(projectService).rejectBid(3L, 7L);

        ResponseEntity<ApiResponse<String>> response = projectController.rejectBid(3L, 7L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("success", response.getBody().getStatus());
        assertEquals("Project rejected", response.getBody().getData());
    }

    @Test
    void rejectBid_whenServiceThrows_propagatesException() {
        doThrow(new IllegalStateException("Only pending bids can be rejected"))
                .when(projectService).rejectBid(3L, 7L);

        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> projectController.rejectBid(3L, 7L));

        assertEquals("Only pending bids can be rejected", ex.getMessage());
    }
}

