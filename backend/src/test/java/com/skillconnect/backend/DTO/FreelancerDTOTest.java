package com.skillconnect.backend.DTO;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FreelancerDTOTest {

    @Test
    void noArgsConstructor_createsInstance() {
        FreelancerDTO dto = new FreelancerDTO();
        
        assertNotNull(dto);
        assertNull(dto.getName());
        assertNull(dto.getEmail());
        assertNull(dto.getRating());
        assertNull(dto.getSkills());
        assertNull(dto.getPastWorks());
    }

    @Test
    void allArgsConstructor_setsAllFields() {
        List<String> skills = Arrays.asList("Java", "Spring Boot");
        List<PastWorkDTO> pastWorks = new ArrayList<>();
        
        FreelancerDTO dto = new FreelancerDTO("John Freelancer", "john@freelancer.com", 4.5, skills, pastWorks);
        
        assertEquals("John Freelancer", dto.getName());
        assertEquals("john@freelancer.com", dto.getEmail());
        assertEquals(4.5, dto.getRating());
        assertEquals(skills, dto.getSkills());
        assertEquals(pastWorks, dto.getPastWorks());
    }

    @Test
    void settersAndGetters_workCorrectly() {
        FreelancerDTO dto = new FreelancerDTO();
        List<String> skills = Arrays.asList("Python", "Django", "PostgreSQL");
        List<PastWorkDTO> pastWorks = Arrays.asList(new PastWorkDTO(), new PastWorkDTO());
        
        dto.setName("Jane Freelancer");
        dto.setEmail("jane@freelancer.com");
        dto.setRating(4.8);
        dto.setSkills(skills);
        dto.setPastWorks(pastWorks);
        
        assertEquals("Jane Freelancer", dto.getName());
        assertEquals("jane@freelancer.com", dto.getEmail());
        assertEquals(4.8, dto.getRating());
        assertEquals(3, dto.getSkills().size());
        assertEquals(2, dto.getPastWorks().size());
    }

    @Test
    void setRating_withMinimumValue_setsCorrectly() {
        FreelancerDTO dto = new FreelancerDTO();
        
        dto.setRating(0.0);
        
        assertEquals(0.0, dto.getRating());
    }

    @Test
    void setRating_withMaximumValue_setsCorrectly() {
        FreelancerDTO dto = new FreelancerDTO();
        
        dto.setRating(5.0);
        
        assertEquals(5.0, dto.getRating());
    }

    @Test
    void setRating_withDecimalValue_setsCorrectly() {
        FreelancerDTO dto = new FreelancerDTO();
        
        dto.setRating(3.75);
        
        assertEquals(3.75, dto.getRating());
    }

    @Test
    void setSkills_withEmptyList_setsCorrectly() {
        FreelancerDTO dto = new FreelancerDTO();
        List<String> emptySkills = new ArrayList<>();
        
        dto.setSkills(emptySkills);
        
        assertNotNull(dto.getSkills());
        assertTrue(dto.getSkills().isEmpty());
    }

    @Test
    void setSkills_withMultipleSkills_setsCorrectly() {
        FreelancerDTO dto = new FreelancerDTO();
        List<String> skills = Arrays.asList("JavaScript", "React", "Node.js", "MongoDB", "AWS");
        
        dto.setSkills(skills);
        
        assertEquals(5, dto.getSkills().size());
        assertTrue(dto.getSkills().contains("JavaScript"));
        assertTrue(dto.getSkills().contains("AWS"));
    }

    @Test
    void setPastWorks_withEmptyList_setsCorrectly() {
        FreelancerDTO dto = new FreelancerDTO();
        List<PastWorkDTO> emptyPastWorks = new ArrayList<>();
        
        dto.setPastWorks(emptyPastWorks);
        
        assertNotNull(dto.getPastWorks());
        assertTrue(dto.getPastWorks().isEmpty());
    }

    @Test
    void setPastWorks_withMultipleWorks_setsCorrectly() {
        FreelancerDTO dto = new FreelancerDTO();
        PastWorkDTO work1 = new PastWorkDTO("Project 1", "http://link1.com", "Description 1", 1L);
        PastWorkDTO work2 = new PastWorkDTO("Project 2", "http://link2.com", "Description 2", 1L);
        PastWorkDTO work3 = new PastWorkDTO("Project 3", "http://link3.com", "Description 3", 1L);
        
        List<PastWorkDTO> pastWorks = Arrays.asList(work1, work2, work3);
        dto.setPastWorks(pastWorks);
        
        assertEquals(3, dto.getPastWorks().size());
        assertEquals("Project 1", dto.getPastWorks().get(0).getTitle());
    }

    @Test
    void setNullValues_setsCorrectly() {
        FreelancerDTO dto = new FreelancerDTO("Name", "email@test.com", 4.0, new ArrayList<>(), new ArrayList<>());
        
        dto.setName(null);
        dto.setEmail(null);
        dto.setRating(null);
        dto.setSkills(null);
        dto.setPastWorks(null);
        
        assertNull(dto.getName());
        assertNull(dto.getEmail());
        assertNull(dto.getRating());
        assertNull(dto.getSkills());
        assertNull(dto.getPastWorks());
    }
}
