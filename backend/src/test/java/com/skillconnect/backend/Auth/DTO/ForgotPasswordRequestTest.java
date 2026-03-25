package com.skillconnect.backend.Auth.DTO;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ForgotPasswordRequestTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void validEmail_passesValidation() {
        ForgotPasswordRequest request = new ForgotPasswordRequest("user@example.com");
        
        Set<ConstraintViolation<ForgotPasswordRequest>> violations = validator.validate(request);
        
        assertTrue(violations.isEmpty());
    }

    @Test
    void blankEmail_failsValidation() {
        ForgotPasswordRequest request = new ForgotPasswordRequest("");
        
        Set<ConstraintViolation<ForgotPasswordRequest>> violations = validator.validate(request);
        
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("email is required")));
    }

    @Test
    void nullEmail_failsValidation() {
        ForgotPasswordRequest request = new ForgotPasswordRequest(null);
        
        Set<ConstraintViolation<ForgotPasswordRequest>> violations = validator.validate(request);
        
        assertFalse(violations.isEmpty());
    }

    @Test
    void invalidEmailFormat_failsValidation() {
        ForgotPasswordRequest request = new ForgotPasswordRequest("invalid-email");
        
        Set<ConstraintViolation<ForgotPasswordRequest>> violations = validator.validate(request);
        
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("invalid email format")));
    }

    @Test
    void emailWithoutDomain_failsValidation() {
        ForgotPasswordRequest request = new ForgotPasswordRequest("user@");
        
        Set<ConstraintViolation<ForgotPasswordRequest>> violations = validator.validate(request);
        
        assertFalse(violations.isEmpty());
    }

    @Test
    void getterAndSetter_workCorrectly() {
        ForgotPasswordRequest request = new ForgotPasswordRequest();
        request.setEmail("test@example.com");
        
        assertEquals("test@example.com", request.getEmail());
    }
}
