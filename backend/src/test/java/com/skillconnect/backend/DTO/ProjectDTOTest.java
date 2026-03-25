package com.skillconnect.backend.DTO;

import com.skillconnect.backend.Entity.Project;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ProjectDTOTest {

    @Test
    void noArgsConstructor_createsInstance() {
        ProjectDTO dto = new ProjectDTO();
        
        assertNotNull(dto);
        assertNull(dto.getId());
        assertNull(dto.getTitle());
        assertNull(dto.getDescription());
        assertNull(dto.getCategory());
        assertNull(dto.getDeadline());
        assertNull(dto.getBudget());
        assertNull(dto.getStatus());
        assertNull(dto.getClientId());
    }

    @Test
    void allArgsConstructor_setsAllFields() {
        LocalDateTime deadline = LocalDateTime.of(2024, 12, 31, 23, 59);
        
        ProjectDTO dto = new ProjectDTO(
            1L,
            "Website Development",
            "Build a responsive website",
            "Web Development",
            deadline,
            5000L,
            Project.ProjectStatus.OPEN,
            10L,
            null,
            null,
            null
        );
        
        assertEquals(1L, dto.getId());
        assertEquals("Website Development", dto.getTitle());
        assertEquals("Build a responsive website", dto.getDescription());
        assertEquals("Web Development", dto.getCategory());
        assertEquals(deadline, dto.getDeadline());
        assertEquals(5000L, dto.getBudget());
        assertEquals(Project.ProjectStatus.OPEN, dto.getStatus());
        assertEquals(10L, dto.getClientId());
    }

    @Test
    void settersAndGetters_workCorrectly() {
        ProjectDTO dto = new ProjectDTO();
        LocalDateTime deadline = LocalDateTime.of(2025, 6, 15, 18, 0);
        
        dto.setId(2L);
        dto.setTitle("Mobile App");
        dto.setDescription("Create a mobile application");
        dto.setCategory("Mobile Development");
        dto.setDeadline(deadline);
        dto.setBudget(10000L);
        dto.setStatus(Project.ProjectStatus.CLOSED);
        dto.setClientId(20L);
        
        assertEquals(2L, dto.getId());
        assertEquals("Mobile App", dto.getTitle());
        assertEquals("Create a mobile application", dto.getDescription());
        assertEquals("Mobile Development", dto.getCategory());
        assertEquals(deadline, dto.getDeadline());
        assertEquals(10000L, dto.getBudget());
        assertEquals(Project.ProjectStatus.CLOSED, dto.getStatus());
        assertEquals(20L, dto.getClientId());
    }

    @Test
    void setStatus_withAllProjectStatuses_setsCorrectly() {
        ProjectDTO dto = new ProjectDTO();
        
        dto.setStatus(Project.ProjectStatus.OPEN);
        assertEquals(Project.ProjectStatus.OPEN, dto.getStatus());
        
        dto.setStatus(Project.ProjectStatus.CLOSED);
        assertEquals(Project.ProjectStatus.CLOSED, dto.getStatus());
    }

    @Test
    void setBudget_withVariousValues_setsCorrectly() {
        ProjectDTO dto = new ProjectDTO();
        
        dto.setBudget(0L);
        assertEquals(0L, dto.getBudget());
        
        dto.setBudget(1000L);
        assertEquals(1000L, dto.getBudget());
        
        dto.setBudget(999999L);
        assertEquals(999999L, dto.getBudget());
    }

    @Test
    void setDeadline_withFutureDate_setsCorrectly() {
        ProjectDTO dto = new ProjectDTO();
        LocalDateTime futureDate = LocalDateTime.of(2026, 12, 31, 23, 59);
        
        dto.setDeadline(futureDate);
        
        assertEquals(futureDate, dto.getDeadline());
    }

    @Test
    void setDeadline_withPastDate_setsCorrectly() {
        ProjectDTO dto = new ProjectDTO();
        LocalDateTime pastDate = LocalDateTime.of(2023, 1, 1, 0, 0);
        
        dto.setDeadline(pastDate);
        
        assertEquals(pastDate, dto.getDeadline());
    }

    @Test
    void setTitle_withLongText_setsCorrectly() {
        ProjectDTO dto = new ProjectDTO();
        String longTitle = "Enterprise-Level Cloud-Based Customer Relationship Management System";
        
        dto.setTitle(longTitle);
        
        assertEquals(longTitle, dto.getTitle());
    }

    @Test
    void setDescription_withLongText_setsCorrectly() {
        ProjectDTO dto = new ProjectDTO();
        String longDescription = "This is a comprehensive project that requires extensive development work " +
                "including frontend, backend, database design, API integration, testing, and deployment.";
        
        dto.setDescription(longDescription);
        
        assertEquals(longDescription, dto.getDescription());
    }

    @Test
    void setCategory_withDifferentCategories_setsCorrectly() {
        ProjectDTO dto = new ProjectDTO();
        
        dto.setCategory("Web Development");
        assertEquals("Web Development", dto.getCategory());
        
        dto.setCategory("Data Science");
        assertEquals("Data Science", dto.getCategory());
        
        dto.setCategory("Graphic Design");
        assertEquals("Graphic Design", dto.getCategory());
    }

    @Test
    void setNullValues_setsCorrectly() {
        LocalDateTime deadline = LocalDateTime.now();
        ProjectDTO dto = new ProjectDTO(1L, "Title", "Description", "Category", deadline, 1000L, Project.ProjectStatus.OPEN, 1L, null, null, null);
        
        dto.setId(null);
        dto.setTitle(null);
        dto.setDescription(null);
        dto.setCategory(null);
        dto.setDeadline(null);
        dto.setBudget(null);
        dto.setStatus(null);
        dto.setClientId(null);
        
        assertNull(dto.getId());
        assertNull(dto.getTitle());
        assertNull(dto.getDescription());
        assertNull(dto.getCategory());
        assertNull(dto.getDeadline());
        assertNull(dto.getBudget());
        assertNull(dto.getStatus());
        assertNull(dto.getClientId());
    }
}
