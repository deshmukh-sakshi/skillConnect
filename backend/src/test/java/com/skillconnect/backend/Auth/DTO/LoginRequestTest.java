package com.skillconnect.backend.Auth.DTO;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class LoginRequestTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void validLoginRequest_passesValidation() {
        LoginRequest request = new LoginRequest("user@example.com", "password123");
        
        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);
        
        assertTrue(violations.isEmpty());
    }

    @Test
    void blankEmail_failsValidation() {
        LoginRequest request = new LoginRequest("", "password123");
        
        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);
        
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("email is required")));
    }

    @Test
    void nullEmail_failsValidation() {
        LoginRequest request = new LoginRequest(null, "password123");
        
        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);
        
        assertFalse(violations.isEmpty());
    }

    @Test
    void invalidEmailFormat_failsValidation() {
        LoginRequest request = new LoginRequest("not-an-email", "password123");
        
        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);
        
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("invalid email format")));
    }

    @Test
    void blankPassword_failsValidation() {
        LoginRequest request = new LoginRequest("user@example.com", "");
        
        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);
        
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("password is required")));
    }

    @Test
    void nullPassword_failsValidation() {
        LoginRequest request = new LoginRequest("user@example.com", null);
        
        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);
        
        assertFalse(violations.isEmpty());
    }

    @Test
    void bothFieldsBlank_failsValidation() {
        LoginRequest request = new LoginRequest("", "");
        
        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);
        
        assertFalse(violations.isEmpty());
        assertTrue(violations.size() >= 2);
    }

    @Test
    void gettersAndSetters_workCorrectly() {
        LoginRequest request = new LoginRequest();
        request.setEmail("test@example.com");
        request.setPassword("testpass");
        
        assertEquals("test@example.com", request.getEmail());
        assertEquals("testpass", request.getPassword());
    }

    @Test
    void allArgsConstructor_setsFieldsCorrectly() {
        LoginRequest request = new LoginRequest("user@test.com", "mypassword");
        
        assertEquals("user@test.com", request.getEmail());
        assertEquals("mypassword", request.getPassword());
    }
}
