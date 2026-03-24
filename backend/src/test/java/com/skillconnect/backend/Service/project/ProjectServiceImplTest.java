package com.skillconnect.backend.Service.project;

import com.skillconnect.backend.DTO.ProjectDTO;
import com.skillconnect.backend.Entity.Client;
import com.skillconnect.backend.Entity.Project;
import com.skillconnect.backend.Repository.ClientRepository;
import com.skillconnect.backend.Repository.ProjectRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceImplTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ProjectServiceImpl projectService;

    @Test
    void createProject_success_mapsAndReturnsDto() {
        ProjectDTO input = new ProjectDTO(
                null,
                "Portfolio website",
                "Build a personal portfolio",
                LocalDateTime.of(2026, 4, 1, 12, 0),
                25000L,
                null,
                7L
        );

        Client client = new Client();
        client.setId(7L);

        when(clientRepository.findById(7L)).thenReturn(Optional.of(client));
        when(projectRepository.save(any(Project.class))).thenAnswer(invocation -> {
            Project p = invocation.getArgument(0);
            p.setId(99L);
            p.setStatus(Project.ProjectStatus.OPEN);
            return p;
        });

        ProjectDTO result = projectService.createProject(input);

        assertEquals(99L, result.getId());
        assertEquals("Portfolio website", result.getTitle());
        assertEquals("Build a personal portfolio", result.getDescription());
        assertEquals(25000L, result.getBudget());
        assertEquals(Project.ProjectStatus.OPEN, result.getStatus());
        assertEquals(7L, result.getClientId());

        ArgumentCaptor<Project> captor = ArgumentCaptor.forClass(Project.class);
        verify(projectRepository).save(captor.capture());
        assertSame(client, captor.getValue().getClient());
    }

    @Test
    void createProject_clientNotFound_throwsRuntimeException() {
        ProjectDTO input = new ProjectDTO(
                null,
                "API Project",
                "No client exists",
                LocalDateTime.of(2026, 5, 1, 9, 30),
                15000L,
                null,
                123L
        );

        when(clientRepository.findById(123L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> projectService.createProject(input));

        assertEquals("Client not found", ex.getMessage());
        verify(projectRepository, never()).save(any(Project.class));
    }

    @Test
    void getProjectById_notFound_throwsRuntimeException() {
        when(projectRepository.findById(44L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> projectService.getProjectById(44L));

        assertEquals("Project not found", ex.getMessage());
    }

    @Test
    void deleteProjectById_whenExists_deletesAndReturnsTrue() {
        when(projectRepository.existsById(5L)).thenReturn(true);

        boolean deleted = projectService.deleteProjectById(5L);

        assertTrue(deleted);
        verify(projectRepository).deleteById(5L);
    }

    @Test
    void deleteProjectById_whenMissing_returnsFalse() {
        when(projectRepository.existsById(6L)).thenReturn(false);

        boolean deleted = projectService.deleteProjectById(6L);

        assertFalse(deleted);
        verify(projectRepository, never()).deleteById(anyLong());
    }
}

