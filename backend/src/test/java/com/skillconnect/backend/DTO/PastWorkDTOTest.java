package com.skillconnect.backend.DTO;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PastWorkDTOTest {

    @Test
    void noArgsConstructor_createsInstance() {
        PastWorkDTO dto = new PastWorkDTO();

        assertNotNull(dto);
        assertNull(dto.getId());
        assertNull(dto.getTitle());
        assertNull(dto.getLink());
        assertNull(dto.getDescription());
    }

    @Test
    void settersAndGetters_workCorrectly() {
        PastWorkDTO dto = new PastWorkDTO();

        dto.setId(1L);
        dto.setTitle("Mobile App");
        dto.setLink("https://app.example.com");
        dto.setDescription("Developed a cross-platform mobile application");

        assertEquals(1L, dto.getId());
        assertEquals("Mobile App", dto.getTitle());
        assertEquals("https://app.example.com", dto.getLink());
        assertEquals("Developed a cross-platform mobile application", dto.getDescription());
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
    void setId_withDifferentValues_setsCorrectly() {
        PastWorkDTO dto = new PastWorkDTO();

        dto.setId(1L);
        assertEquals(1L, dto.getId());

        dto.setId(999L);
        assertEquals(999L, dto.getId());

        dto.setId(0L);
        assertEquals(0L, dto.getId());
    }

    @Test
    void setNullValues_setsCorrectly() {
        PastWorkDTO dto = new PastWorkDTO();
        dto.setId(1L);
        dto.setTitle("Title");
        dto.setLink("Link");
        dto.setDescription("Description");

        dto.setId(null);
        dto.setTitle(null);
        dto.setLink(null);
        dto.setDescription(null);

        assertNull(dto.getId());
        assertNull(dto.getTitle());
        assertNull(dto.getLink());
        assertNull(dto.getDescription());
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
