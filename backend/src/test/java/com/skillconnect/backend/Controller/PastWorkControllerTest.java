package com.skillconnect.backend.Controller;

import com.skillconnect.backend.DTO.ApiResponse;
import com.skillconnect.backend.DTO.PastWorkDTO;
import com.skillconnect.backend.Entity.PastWork;
import com.skillconnect.backend.Service.pastWork.PastWorkService;
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
class PastWorkControllerTest {

    @Mock
    private PastWorkService pastWorkService;

    @InjectMocks
    private PastWorkController pastWorkController;

    @Test
    void addPastWork_returnsCreatedEnvelope() {
        PastWorkDTO request = new PastWorkDTO("Portfolio", "https://example.com", "Client project", 4L);
        PastWorkDTO saved = new PastWorkDTO("Portfolio", "https://example.com", "Client project", 4L);

        when(pastWorkService.addPastWork(request)).thenReturn(saved);

        ResponseEntity<ApiResponse<PastWorkDTO>> response = pastWorkController.addPastWork(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("success", response.getBody().getStatus());
        assertEquals(4L, response.getBody().getData().getFreelancerId());
    }

    @Test
    void updatePastWork_returnsSuccessEnvelope() {
        PastWorkDTO request = new PastWorkDTO("Updated", "https://link", "desc", 4L);
        PastWork saved = new PastWork();
        saved.setId(9L);
        saved.setTitle("Updated");

        when(pastWorkService.updatePastWork(9L, request)).thenReturn(saved);

        ResponseEntity<ApiResponse<PastWork>> response = pastWorkController.updatePastWork(9L, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("success", response.getBody().getStatus());
        assertEquals(9L, response.getBody().getData().getId());
    }

    @Test
    void updatePastWork_whenServiceThrows_propagatesException() {
        PastWorkDTO request = new PastWorkDTO("Updated", "https://link", "desc", 4L);
        when(pastWorkService.updatePastWork(9L, request)).thenThrow(new RuntimeException("Past work not found"));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> pastWorkController.updatePastWork(9L, request));

        assertEquals("Past work not found", ex.getMessage());
    }

    @Test
    void deletePastWork_returnsSuccessEnvelope() {
        doNothing().when(pastWorkService).deletePastWork(5L);

        ResponseEntity<ApiResponse<String>> response = pastWorkController.deletePastWork(5L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("success", response.getBody().getStatus());
        assertEquals("Past work deleted.", response.getBody().getData());
    }

    @Test
    void deletePastWork_whenServiceThrows_propagatesException() {
        doThrow(new RuntimeException("Past work not found")).when(pastWorkService).deletePastWork(5L);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> pastWorkController.deletePastWork(5L));

        assertEquals("Past work not found", ex.getMessage());
    }
}

