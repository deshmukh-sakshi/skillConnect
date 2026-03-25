package com.skillconnect.backend.DTO;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PastWorkDTOTest {

    @Test
    void noArgsConstructor_createsInstance() {
        PastWorkDTO dto = new PastWorkDTO();
        
        assertNotNull(dto);
        assertNull(dto.getTitle());
        assertNull(dto.getLink());
        assertNull(dto.getDescription());
        assertNull(dto.getFreelancerId());
    }

    @Test
    void allArgsConstructor_setsAllFields() {
        PastWorkDTO dto = new PastWorkDTO("E-commerce Website", "https://example.com", "Built a full-stack e-commerce platform", 1L);
        
        assertEquals("E-commerce Website", dto.getTitle());
        assertEquals("https://example.com", dto.getLink());
        assertEquals("Built a full-stack e-commerce platform", dto.getDescription());
        assertEquals(1L, dto.getFreelancerId());
    }

    @Test
    void settersAndGetters_workCorrectly() {
        PastWorkDTO dto = new PastWorkDTO();
        
        dto.setTitle("Mobile App");
        dto.setLink("https://app.example.com");
        dto.setDescription("Developed a cross-platform mobile application");
        dto.setFreelancerId(5L);
        
        assertEquals("Mobile App", dto.getTitle());
        assertEquals("https://app.example.com", dto.getLink());
        assertEquals("Developed a cross-platform mobile application", dto.getDescription());
        assertEquals(5L, dto.getFreelancerId());
    }

    @Test
    void setTitle_withLongText_setsCorrectly() {
        PastWorkDTO dto = new PastWorkDTO();
        String longTitle = "Enterprise Resource Planning System with Advanced Analytics and Reporting";
        
        dto.setTitle(longTitle);
        
        assertEquals(longTitle, dto.getTitle());
    }

    @Test
    void setLink_withVariousURLFormats_setsCorrectly() {
        PastWorkDTO dto = new PastWorkDTO();
        
        dto.setLink("https://github.com/user/repo");
        assertEquals("https://github.com/user/repo", dto.getLink());
        
        dto.setLink("http://portfolio.com/project");
        assertEquals("http://portfolio.com/project", dto.getLink());
        
        dto.setLink("https://www.behance.net/gallery/123456");
        assertEquals("https://www.behance.net/gallery/123456", dto.getLink());
    }

    @Test
    void setDescription_withLongText_setsCorrectly() {
        PastWorkDTO dto = new PastWorkDTO();
        String longDescription = "This project involved designing and implementing a comprehensive solution " +
                "that included frontend development with React, backend API with Spring Boot, " +
                "database design with PostgreSQL, and deployment on AWS infrastructure.";
        
        dto.setDescription(longDescription);
        
        assertEquals(longDescription, dto.getDescription());
    }

    @Test
    void setFreelancerId_withDifferentValues_setsCorrectly() {
        PastWorkDTO dto = new PastWorkDTO();
        
        dto.setFreelancerId(1L);
        assertEquals(1L, dto.getFreelancerId());
        
        dto.setFreelancerId(999L);
        assertEquals(999L, dto.getFreelancerId());
        
        dto.setFreelancerId(0L);
        assertEquals(0L, dto.getFreelancerId());
    }

    @Test
    void setNullValues_setsCorrectly() {
        PastWorkDTO dto = new PastWorkDTO("Title", "Link", "Description", 1L);
        
        dto.setTitle(null);
        dto.setLink(null);
        dto.setDescription(null);
        dto.setFreelancerId(null);
        
        assertNull(dto.getTitle());
        assertNull(dto.getLink());
        assertNull(dto.getDescription());
        assertNull(dto.getFreelancerId());
    }

    @Test
    void setEmptyStrings_setsCorrectly() {
        PastWorkDTO dto = new PastWorkDTO();
        
        dto.setTitle("");
        dto.setLink("");
        dto.setDescription("");
        
        assertEquals("", dto.getTitle());
        assertEquals("", dto.getLink());
        assertEquals("", dto.getDescription());
    }
}
