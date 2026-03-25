package com.skillconnect.backend.DTO;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SkillDTOTest {

    @Test
    void noArgsConstructor_createsInstance() {
        SkillDTO dto = new SkillDTO();
        
        assertNotNull(dto);
        assertNull(dto.getId());
        assertNull(dto.getSkillName());
        assertNull(dto.getFreelancerId());
    }

    @Test
    void allArgsConstructor_setsAllFields() {
        SkillDTO dto = new SkillDTO(1L, "Java", 10L);
        
        assertEquals(1L, dto.getId());
        assertEquals("Java", dto.getSkillName());
        assertEquals(10L, dto.getFreelancerId());
    }

    @Test
    void settersAndGetters_workCorrectly() {
        SkillDTO dto = new SkillDTO();
        
        dto.setId(5L);
        dto.setSkillName("Python");
        dto.setFreelancerId(20L);
        
        assertEquals(5L, dto.getId());
        assertEquals("Python", dto.getSkillName());
        assertEquals(20L, dto.getFreelancerId());
    }

    @Test
    void setSkillName_withVariousSkills_setsCorrectly() {
        SkillDTO dto = new SkillDTO();
        
        dto.setSkillName("JavaScript");
        assertEquals("JavaScript", dto.getSkillName());
        
        dto.setSkillName("Spring Boot");
        assertEquals("Spring Boot", dto.getSkillName());
        
        dto.setSkillName("React");
        assertEquals("React", dto.getSkillName());
        
        dto.setSkillName("PostgreSQL");
        assertEquals("PostgreSQL", dto.getSkillName());
    }

    @Test
    void setSkillName_withLongName_setsCorrectly() {
        SkillDTO dto = new SkillDTO();
        String longSkillName = "Advanced Machine Learning and Artificial Intelligence";
        
        dto.setSkillName(longSkillName);
        
        assertEquals(longSkillName, dto.getSkillName());
    }

    @Test
    void setId_withDifferentValues_setsCorrectly() {
        SkillDTO dto = new SkillDTO();
        
        dto.setId(0L);
        assertEquals(0L, dto.getId());
        
        dto.setId(100L);
        assertEquals(100L, dto.getId());
        
        dto.setId(999999L);
        assertEquals(999999L, dto.getId());
    }

    @Test
    void setFreelancerId_withDifferentValues_setsCorrectly() {
        SkillDTO dto = new SkillDTO();
        
        dto.setFreelancerId(1L);
        assertEquals(1L, dto.getFreelancerId());
        
        dto.setFreelancerId(500L);
        assertEquals(500L, dto.getFreelancerId());
    }

    @Test
    void setNullValues_setsCorrectly() {
        SkillDTO dto = new SkillDTO(1L, "Java", 10L);
        
        dto.setId(null);
        dto.setSkillName(null);
        dto.setFreelancerId(null);
        
        assertNull(dto.getId());
        assertNull(dto.getSkillName());
        assertNull(dto.getFreelancerId());
    }

    @Test
    void setEmptySkillName_setsCorrectly() {
        SkillDTO dto = new SkillDTO();
        
        dto.setSkillName("");
        
        assertEquals("", dto.getSkillName());
    }

    @Test
    void multipleSkillsForSameFreelancer_workCorrectly() {
        SkillDTO skill1 = new SkillDTO(1L, "Java", 100L);
        SkillDTO skill2 = new SkillDTO(2L, "Spring Boot", 100L);
        SkillDTO skill3 = new SkillDTO(3L, "Docker", 100L);
        
        assertEquals(100L, skill1.getFreelancerId());
        assertEquals(100L, skill2.getFreelancerId());
        assertEquals(100L, skill3.getFreelancerId());
        
        assertNotEquals(skill1.getId(), skill2.getId());
        assertNotEquals(skill2.getId(), skill3.getId());
    }
}
