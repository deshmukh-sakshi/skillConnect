package com.skillconnect.backend.Auth.DTO;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ResetPasswordRequestTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void validRequest_passesValidation() {
        ResetPasswordRequest request = new ResetPasswordRequest("valid-token", "password123");
        
        Set<ConstraintViolation<ResetPasswordRequest>> violations = validator.validate(request);
        
        assertTrue(violations.isEmpty());
    }

    @Test
    void blankToken_failsValidation() {
        ResetPasswordRequest request = new ResetPasswordRequest("", "password123");
        
        Set<ConstraintViolation<ResetPasswordRequest>> violations = validator.validate(request);
        
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("reset token is required")));
    }

    @Test
    void nullToken_failsValidation() {
        ResetPasswordRequest request = new ResetPasswordRequest(null, "password123");
        
        Set<ConstraintViolation<ResetPasswordRequest>> violations = validator.validate(request);
        
        assertFalse(violations.isEmpty());
    }

    @Test
    void blankPassword_failsValidation() {
        ResetPasswordRequest request = new ResetPasswordRequest("valid-token", "");
        
        Set<ConstraintViolation<ResetPasswordRequest>> violations = validator.validate(request);
        
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("password is required")));
    }

    @Test
    void passwordTooShort_failsValidation() {
        ResetPasswordRequest request = new ResetPasswordRequest("valid-token", "short");
        
        Set<ConstraintViolation<ResetPasswordRequest>> violations = validator.validate(request);
        
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("password must be between 8 and 20 characters")));
    }

    @Test
    void passwordTooLong_failsValidation() {
        ResetPasswordRequest request = new ResetPasswordRequest("valid-token", "a".repeat(21));
        
        Set<ConstraintViolation<ResetPasswordRequest>> violations = validator.validate(request);
        
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("password must be between 8 and 20 characters")));
    }

    @Test
    void passwordExactly8Characters_passesValidation() {
        ResetPasswordRequest request = new ResetPasswordRequest("valid-token", "12345678");
        
        Set<ConstraintViolation<ResetPasswordRequest>> violations = validator.validate(request);
        
        assertTrue(violations.isEmpty());
    }

    @Test
    void passwordExactly20Characters_passesValidation() {
        ResetPasswordRequest request = new ResetPasswordRequest("valid-token", "a".repeat(20));
        
        Set<ConstraintViolation<ResetPasswordRequest>> violations = validator.validate(request);
        
        assertTrue(violations.isEmpty());
    }

    @Test
    void gettersAndSetters_workCorrectly() {
        ResetPasswordRequest request = new ResetPasswordRequest();
        request.setToken("test-token");
        request.setPassword("testpass123");
        
        assertEquals("test-token", request.getToken());
        assertEquals("testpass123", request.getPassword());
    }
}
