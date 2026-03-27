package com.skillconnect.backend.Controller;

import com.skillconnect.backend.DTO.ApiResponse;
import com.skillconnect.backend.DTO.ClientDTO;
import com.skillconnect.backend.Service.client.ClientService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientControllerTest {

    @Mock
    private ClientService clientService;

    @InjectMocks
    private ClientController clientController;

    @Test
    void getClient_whenFound_returnsOk() {
        ClientDTO dto = new ClientDTO();
        dto.setName("Acme");

        when(clientService.getClientDTOById(1L)).thenReturn(dto);

        ResponseEntity<ApiResponse<ClientDTO>> response = clientController.getClient(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSuccess());
        assertEquals("Acme", response.getBody().getData().getName());
    }

    @Test
    void getClient_whenMissing_returnsNotFound() {
        when(clientService.getClientDTOById(5L)).thenReturn(null);

        ResponseEntity<ApiResponse<ClientDTO>> response = clientController.getClient(5L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        assertNull(response.getBody().getData());
    }

    @Test
    void deleteClient_whenDeleted_returnsSuccessMessage() {
        when(clientService.deleteClient(4L)).thenReturn(true);

        ResponseEntity<ApiResponse<String>> response = clientController.deleteClient(4L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSuccess());
        assertEquals("Client deleted successfully", response.getBody().getData());
    }

    @Test
    void deleteClient_whenMissing_returnsNotFoundMessage() {
        when(clientService.deleteClient(4L)).thenReturn(false);

        ResponseEntity<ApiResponse<String>> response = clientController.deleteClient(4L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        assertEquals("Client not found", response.getBody().getError());
    }
}

