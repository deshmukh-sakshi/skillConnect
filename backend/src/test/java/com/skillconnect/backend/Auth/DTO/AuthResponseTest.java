package com.skillconnect.backend.Auth.DTO;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthResponseTest {

    @Test
    void builder_createsInstanceWithAllFields() {
        Long id = 1L;
        String role = "ROLE_CLIENT";
        String name = "John Doe";
        String email = "john@example.com";
        String token = "jwt-token-123";
        
        AuthResponse response = AuthResponse.builder()
                .id(id)
                .role(role)
                .name(name)
                .email(email)
                .token(token)
                .build();
        
        assertNotNull(response);
        assertEquals(id, response.getId());
        assertEquals(role, response.getRole());
        assertEquals(name, response.getName());
        assertEquals(email, response.getEmail());
        assertEquals(token, response.getToken());
    }

    @Test
    void builder_createsInstanceWithNullValues() {
        AuthResponse response = AuthResponse.builder()
                .id(null)
                .role(null)
                .name(null)
                .email(null)
                .token(null)
                .build();
        
        assertNotNull(response);
        assertNull(response.getId());
        assertNull(response.getRole());
        assertNull(response.getName());
        assertNull(response.getEmail());
        assertNull(response.getToken());
    }

    @Test
    void builder_createsClientResponse() {
        AuthResponse response = AuthResponse.builder()
                .id(10L)
                .role("ROLE_CLIENT")
                .name("Client User")
                .email("client@test.com")
                .token("client-token")
                .build();
        
        assertEquals(10L, response.getId());
        assertEquals("ROLE_CLIENT", response.getRole());
        assertEquals("Client User", response.getName());
        assertEquals("client@test.com", response.getEmail());
        assertEquals("client-token", response.getToken());
    }

    @Test
    void builder_createsFreelancerResponse() {
        AuthResponse response = AuthResponse.builder()
                .id(20L)
                .role("ROLE_FREELANCER")
                .name("Freelancer User")
                .email("freelancer@test.com")
                .token("freelancer-token")
                .build();
        
        assertEquals(20L, response.getId());
        assertEquals("ROLE_FREELANCER", response.getRole());
        assertEquals("Freelancer User", response.getName());
        assertEquals("freelancer@test.com", response.getEmail());
        assertEquals("freelancer-token", response.getToken());
    }

    @Test
    void getters_returnCorrectValues() {
        AuthResponse response = AuthResponse.builder()
                .id(5L)
                .role("ROLE_CLIENT")
                .name("Test User")
                .email("test@example.com")
                .token("test-token")
                .build();
        
        assertEquals(5L, response.getId());
        assertEquals("ROLE_CLIENT", response.getRole());
        assertEquals("Test User", response.getName());
        assertEquals("test@example.com", response.getEmail());
        assertEquals("test-token", response.getToken());
    }
}
