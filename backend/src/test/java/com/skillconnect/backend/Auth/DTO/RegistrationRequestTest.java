package com.skillconnect.backend.Auth.DTO;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class RegistrationRequestTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void validRegistrationRequest_passesValidation() {
        RegistrationRequest request = new RegistrationRequest("John Doe", "john@example.com", "password123");
        
        Set<ConstraintViolation<RegistrationRequest>> violations = validator.validate(request);
        
        assertTrue(violations.isEmpty());
    }

    @Test
    void blankName_failsValidation() {
        RegistrationRequest request = new RegistrationRequest("", "john@example.com", "password123");
        
        Set<ConstraintViolation<RegistrationRequest>> violations = validator.validate(request);
        
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("name is required")));
    }

    @Test
    void nullName_failsValidation() {
        RegistrationRequest request = new RegistrationRequest(null, "john@example.com", "password123");
        
        Set<ConstraintViolation<RegistrationRequest>> violations = validator.validate(request);
        
        assertFalse(violations.isEmpty());
    }

    @Test
    void blankEmail_failsValidation() {
        RegistrationRequest request = new RegistrationRequest("John Doe", "", "password123");
        
        Set<ConstraintViolation<RegistrationRequest>> violations = validator.validate(request);
        
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("email is required")));
    }

    @Test
    void invalidEmailFormat_failsValidation() {
        RegistrationRequest request = new RegistrationRequest("John Doe", "invalid-email", "password123");
        
        Set<ConstraintViolation<RegistrationRequest>> violations = validator.validate(request);
        
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("invalid email format")));
    }

    @Test
    void blankPassword_failsValidation() {
        RegistrationRequest request = new RegistrationRequest("John Doe", "john@example.com", "");
        
        Set<ConstraintViolation<RegistrationRequest>> violations = validator.validate(request);
        
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("password is required")));
    }

    @Test
    void passwordTooShort_failsValidation() {
        RegistrationRequest request = new RegistrationRequest("John Doe", "john@example.com", "short");
        
        Set<ConstraintViolation<RegistrationRequest>> violations = validator.validate(request);
        
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("password must be between 8 and 20 characters")));
    }

    @Test
    void passwordTooLong_failsValidation() {
        RegistrationRequest request = new RegistrationRequest("John Doe", "john@example.com", "a".repeat(21));
        
        Set<ConstraintViolation<RegistrationRequest>> violations = validator.validate(request);
        
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("password must be between 8 and 20 characters")));
    }

    @Test
    void passwordExactly8Characters_passesValidation() {
        RegistrationRequest request = new RegistrationRequest("John Doe", "john@example.com", "12345678");
        
        Set<ConstraintViolation<RegistrationRequest>> violations = validator.validate(request);
        
        assertTrue(violations.isEmpty());
    }

    @Test
    void passwordExactly20Characters_passesValidation() {
        RegistrationRequest request = new RegistrationRequest("John Doe", "john@example.com", "a".repeat(20));
        
        Set<ConstraintViolation<RegistrationRequest>> violations = validator.validate(request);
        
        assertTrue(violations.isEmpty());
    }

    @Test
    void allFieldsBlank_failsValidation() {
        RegistrationRequest request = new RegistrationRequest("", "", "");
        
        Set<ConstraintViolation<RegistrationRequest>> violations = validator.validate(request);
        
        assertFalse(violations.isEmpty());
        assertTrue(violations.size() >= 3);
    }

    @Test
    void gettersAndSetters_workCorrectly() {
        RegistrationRequest request = new RegistrationRequest();
        request.setName("Jane Doe");
        request.setEmail("jane@example.com");
        request.setPassword("password456");
        
        assertEquals("Jane Doe", request.getName());
        assertEquals("jane@example.com", request.getEmail());
        assertEquals("password456", request.getPassword());
    }

    @Test
    void allArgsConstructor_setsFieldsCorrectly() {
        RegistrationRequest request = new RegistrationRequest("Bob Smith", "bob@test.com", "bobpass123");
        
        assertEquals("Bob Smith", request.getName());
        assertEquals("bob@test.com", request.getEmail());
        assertEquals("bobpass123", request.getPassword());
    }
}
