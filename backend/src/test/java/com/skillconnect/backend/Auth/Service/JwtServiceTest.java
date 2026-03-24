package com.skillconnect.backend.Auth.Service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(
                jwtService,
                "secretKey",
                "YWJjZGVmZ2hpamtsbW5vcHFyc3R1dnd4eXoxMjM0NTY3ODkwQUJDREVGR0hJSktMTU5PUFFSU1RVVldYWVoxMjM0NTY="
        );
    }

    @Test
    void generateAndValidateToken_roundTripWorks() {
        UserDetails user = new User(
                "client@test.com",
                "pwd",
                List.of(new SimpleGrantedAuthority("ROLE_CLIENT"))
        );

        String token = jwtService.generateToken(user);

        assertNotNull(token);
        assertEquals("client@test.com", jwtService.extractUsername(token));
        assertTrue(jwtService.isTokenValid(token, user));
    }

    @Test
    void isTokenValid_withDifferentUser_returnsFalse() {
        UserDetails tokenUser = new User("a@test.com", "pwd", List.of(new SimpleGrantedAuthority("ROLE_CLIENT")));
        UserDetails differentUser = new User("b@test.com", "pwd", List.of(new SimpleGrantedAuthority("ROLE_CLIENT")));

        String token = jwtService.generateToken(tokenUser);

        assertFalse(jwtService.isTokenValid(token, differentUser));
    }
}

