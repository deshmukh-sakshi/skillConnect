package com.skillconnect.backend.Controller;

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

        ResponseEntity<ClientDTO> response = clientController.getClient(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Acme", response.getBody().getName());
    }

    @Test
    void getClient_whenMissing_returnsNotFound() {
        when(clientService.getClientDTOById(5L)).thenReturn(null);

        ResponseEntity<ClientDTO> response = clientController.getClient(5L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void deleteClient_whenDeleted_returnsSuccessMessage() {
        when(clientService.deleteClient(4L)).thenReturn(true);

        ResponseEntity<String> response = clientController.deleteClient(4L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Client deleted successfully", response.getBody());
    }

    @Test
    void deleteClient_whenMissing_returnsNotFoundMessage() {
        when(clientService.deleteClient(4L)).thenReturn(false);

        ResponseEntity<String> response = clientController.deleteClient(4L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Client not found", response.getBody());
    }
}

