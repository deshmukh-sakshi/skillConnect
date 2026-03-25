package com.skillconnect.backend.DTO;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ClientDTOTest {

    @Test
    void noArgsConstructor_createsInstance() {
        ClientDTO dto = new ClientDTO();
        
        assertNotNull(dto);
        assertNull(dto.getName());
        assertNull(dto.getEmail());
        assertNull(dto.getProjects());
    }

    @Test
    void allArgsConstructor_setsAllFields() {
        List<ProjectDTO> projects = new ArrayList<>();
        ClientDTO dto = new ClientDTO("John Client", "john@client.com", projects);
        
        assertEquals("John Client", dto.getName());
        assertEquals("john@client.com", dto.getEmail());
        assertEquals(projects, dto.getProjects());
    }

    @Test
    void settersAndGetters_workCorrectly() {
        ClientDTO dto = new ClientDTO();
        List<ProjectDTO> projects = Arrays.asList(
            new ProjectDTO(),
            new ProjectDTO()
        );
        
        dto.setName("Jane Client");
        dto.setEmail("jane@client.com");
        dto.setProjects(projects);
        
        assertEquals("Jane Client", dto.getName());
        assertEquals("jane@client.com", dto.getEmail());
        assertEquals(2, dto.getProjects().size());
    }

    @Test
    void setProjects_withEmptyList_setsCorrectly() {
        ClientDTO dto = new ClientDTO();
        List<ProjectDTO> emptyProjects = new ArrayList<>();
        
        dto.setProjects(emptyProjects);
        
        assertNotNull(dto.getProjects());
        assertTrue(dto.getProjects().isEmpty());
    }

    @Test
    void setProjects_withMultipleProjects_setsCorrectly() {
        ClientDTO dto = new ClientDTO();
        ProjectDTO project1 = new ProjectDTO();
        project1.setTitle("Project 1");
        ProjectDTO project2 = new ProjectDTO();
        project2.setTitle("Project 2");
        ProjectDTO project3 = new ProjectDTO();
        project3.setTitle("Project 3");
        
        List<ProjectDTO> projects = Arrays.asList(project1, project2, project3);
        dto.setProjects(projects);
        
        assertEquals(3, dto.getProjects().size());
        assertEquals("Project 1", dto.getProjects().get(0).getTitle());
        assertEquals("Project 2", dto.getProjects().get(1).getTitle());
        assertEquals("Project 3", dto.getProjects().get(2).getTitle());
    }

    @Test
    void setNullValues_setsCorrectly() {
        ClientDTO dto = new ClientDTO("Name", "email@test.com", new ArrayList<>());
        
        dto.setName(null);
        dto.setEmail(null);
        dto.setProjects(null);
        
        assertNull(dto.getName());
        assertNull(dto.getEmail());
        assertNull(dto.getProjects());
    }

    @Test
    void setEmail_withValidFormat_setsCorrectly() {
        ClientDTO dto = new ClientDTO();
        
        dto.setEmail("valid.email@example.com");
        
        assertEquals("valid.email@example.com", dto.getEmail());
    }
}
