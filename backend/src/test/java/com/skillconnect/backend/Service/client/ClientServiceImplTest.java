package com.skillconnect.backend.Service.client;

import com.skillconnect.backend.DTO.ClientDTO;
import com.skillconnect.backend.Entity.Client;
import com.skillconnect.backend.Entity.Project;
import com.skillconnect.backend.Repository.ClientRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceImplTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientServiceImpl clientService;

    @Test
    void getClientDTOById_whenFound_mapsClientAndProjects() {
        Client client = new Client();
        client.setId(10L);
        client.setName("Acme");

        Project project = new Project();
        project.setId(11L);
        project.setTitle("Website");
        project.setDescription("Build website");
        project.setBudget(5000L);
        project.setClient(client);

        client.setProjects(List.of(project));

        when(clientRepository.findById(10L)).thenReturn(Optional.of(client));

        ClientDTO dto = clientService.getClientDTOById(10L);

        assertNotNull(dto);
        assertEquals("Acme", dto.getName());
        assertNull(dto.getEmail());
        assertEquals(1, dto.getProjects().size());
        assertEquals(11L, dto.getProjects().getFirst().getId());
        assertEquals(10L, dto.getProjects().getFirst().getClientId());
    }

    @Test
    void getClientDTOById_whenMissing_returnsNull() {
        when(clientRepository.findById(99L)).thenReturn(Optional.empty());

        ClientDTO dto = clientService.getClientDTOById(99L);

        assertNull(dto);
    }

    @Test
    void deleteClient_whenExists_deletesAndReturnsTrue() {
        when(clientRepository.existsById(2L)).thenReturn(true);

        boolean deleted = clientService.deleteClient(2L);

        assertTrue(deleted);
        verify(clientRepository).deleteById(2L);
    }

    @Test
    void deleteClient_whenMissing_returnsFalse() {
        when(clientRepository.existsById(3L)).thenReturn(false);

        boolean deleted = clientService.deleteClient(3L);

        assertFalse(deleted);
        verify(clientRepository, never()).deleteById(anyLong());
    }
}

