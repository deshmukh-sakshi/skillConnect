package com.skillconnect.backend.Auth.DTO;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordResponseTest {

    @Test
    void builder_createsInstanceWithMessage() {
        String message = "Password reset successful";
        
        PasswordResponse response = PasswordResponse.builder()
                .message(message)
                .build();
        
        assertNotNull(response);
        assertEquals(message, response.getMessage());
    }

    @Test
    void builder_createsInstanceWithNullMessage() {
        PasswordResponse response = PasswordResponse.builder()
                .message(null)
                .build();
        
        assertNotNull(response);
        assertNull(response.getMessage());
    }

    @Test
    void builder_createsInstanceWithEmptyMessage() {
        PasswordResponse response = PasswordResponse.builder()
                .message("")
                .build();
        
        assertNotNull(response);
        assertEquals("", response.getMessage());
    }

    @Test
    void getMessage_returnsCorrectValue() {
        String expectedMessage = "Password has been reset";
        
        PasswordResponse response = PasswordResponse.builder()
                .message(expectedMessage)
                .build();
        
        assertEquals(expectedMessage, response.getMessage());
    }
}
