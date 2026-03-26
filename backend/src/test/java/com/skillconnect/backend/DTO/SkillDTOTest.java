package com.skillconnect.backend.DTO;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SkillDTOTest {

    @Test
    void noArgsConstructor_createsInstance() {
        PastWorkUpdateDTO dto = new PastWorkUpdateDTO();

        assertNotNull(dto);
        assertNull(dto.getId());
        assertNull(dto.getTitle());
        assertNull(dto.getLink());
        assertNull(dto.getDescription());
        assertNull(dto.getToDelete());
    }

    @Test
    void settersAndGetters_workCorrectly() {
        PastWorkUpdateDTO dto = new PastWorkUpdateDTO();

        dto.setId(1L);
        dto.setTitle("My Project");
        dto.setLink("https://example.com");
        dto.setDescription("A great project");
        dto.setToDelete(false);

        assertEquals(1L, dto.getId());
        assertEquals("My Project", dto.getTitle());
        assertEquals("https://example.com", dto.getLink());
        assertEquals("A great project", dto.getDescription());
        assertFalse(dto.getToDelete());
    }

    @Test
    void setToDelete_true_setsCorrectly() {
        PastWorkUpdateDTO dto = new PastWorkUpdateDTO();

        dto.setTitle("Old Work");
        dto.setLink("https://old.com");
        dto.setDescription("desc");
        dto.setId(5L);
        dto.setToDelete(true);

        assertTrue(dto.getToDelete());
        assertEquals(5L, dto.getId());
    }

    @Test
    void setTitle_withVariousValues_setsCorrectly() {
        PastWorkUpdateDTO dto = new PastWorkUpdateDTO();

        dto.setTitle("JavaScript App");
        assertEquals("JavaScript App", dto.getTitle());

        dto.setTitle("Spring Boot API");
        assertEquals("Spring Boot API", dto.getTitle());
    }

    @Test
    void setLink_withDifferentURLs_setsCorrectly() {
        PastWorkUpdateDTO dto = new PastWorkUpdateDTO();

        dto.setLink("https://github.com/user/repo");
        assertEquals("https://github.com/user/repo", dto.getLink());

        dto.setLink("http://portfolio.com/project");
        assertEquals("http://portfolio.com/project", dto.getLink());
    }

    @Test
    void setId_withDifferentValues_setsCorrectly() {
        PastWorkUpdateDTO dto = new PastWorkUpdateDTO();

        dto.setId(0L);
        assertEquals(0L, dto.getId());

        dto.setId(999999L);
        assertEquals(999999L, dto.getId());
    }

    @Test
    void setNullValues_setsCorrectly() {
        PastWorkUpdateDTO dto = new PastWorkUpdateDTO();
        dto.setId(1L);
        dto.setTitle("Title");
        dto.setLink("Link");
        dto.setDescription("Desc");
        dto.setToDelete(true);

        dto.setId(null);
        dto.setTitle(null);
        dto.setLink(null);
        dto.setDescription(null);
        dto.setToDelete(null);

        assertNull(dto.getId());
        assertNull(dto.getTitle());
        assertNull(dto.getLink());
        assertNull(dto.getDescription());
        assertNull(dto.getToDelete());
    }

    @Test
    void setEmptyStrings_setsCorrectly() {
        PastWorkUpdateDTO dto = new PastWorkUpdateDTO();

        dto.setTitle("");
        dto.setLink("");
        dto.setDescription("");

        assertEquals("", dto.getTitle());
        assertEquals("", dto.getLink());
        assertEquals("", dto.getDescription());
    }
}
