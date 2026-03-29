package com.skillconnect.backend.Exception;

import com.skillconnect.backend.DTO.ApiResponse;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.lang.reflect.Method;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleRuntimeException_returnsBadRequestErrorEnvelope() {
        ResponseEntity<ApiResponse<Object>> response =
                handler.handleRuntimeException(new RuntimeException("Client not found"));

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("error", response.getBody().getStatus());

        @SuppressWarnings("unchecked")
        Map<String, String> error = (Map<String, String>) response.getBody().getError();
        assertEquals("Client not found", error.get("message"));
    }

    @Test
    void handleBadCredentialsException_returnsUnauthorizedErrorEnvelope() {
        ResponseEntity<ApiResponse<Object>> response =
                handler.handleBadCredentialsException(new BadCredentialsException("bad creds"));

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("error", response.getBody().getStatus());

        @SuppressWarnings("unchecked")
        Map<String, String> error = (Map<String, String>) response.getBody().getError();
        assertEquals("Invalid email or password", error.get("message"));
    }

    @Test
    void handleGenericException_returnsInternalServerErrorEnvelope() {
        ResponseEntity<ApiResponse<Object>> response =
                handler.handleGenericException(new Exception("boom"));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("error", response.getBody().getStatus());

        @SuppressWarnings("unchecked")
        Map<String, String> error = (Map<String, String>) response.getBody().getError();
        assertEquals("An unexpected error occurred", error.get("message"));
        assertEquals("boom", error.get("error"));
    }

    @Test
    void handleValidationExceptions_returnsFieldErrorMap() throws NoSuchMethodException {
        Method method = ValidationTarget.class.getMethod("submit", String.class);
        MethodParameter parameter = new MethodParameter(method, 0);

        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult("target", "target");
        bindingResult.addError(new FieldError("target", "email", "invalid email format"));

        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(parameter, bindingResult);

        ResponseEntity<ApiResponse<Object>> response = handler.handleValidationExceptions(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("error", response.getBody().getStatus());

        @SuppressWarnings("unchecked")
        Map<String, String> errors = (Map<String, String>) response.getBody().getError();
        assertEquals("invalid email format", errors.get("email"));
    }

    static class ValidationTarget {
        @SuppressWarnings("unused")
        public void submit(String email) {
        }
    }
}

