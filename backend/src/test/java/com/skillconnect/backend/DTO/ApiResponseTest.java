package com.skillconnect.backend.DTO;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ApiResponseTest {

    @Test
    void success_setsStatusAndData() {
        ApiResponse<String> response = ApiResponse.success("ok");

        assertEquals("success", response.getStatus());
        assertEquals("ok", response.getData());
        assertNull(response.getError());
    }

    @Test
    void error_setsStatusAndError() {
        ApiResponse<Map<String, String>> response = ApiResponse.error(Map.of("message", "bad"));

        assertEquals("error", response.getStatus());
        assertNull(response.getData());
        assertEquals("bad", ((Map<?, ?>) response.getError()).get("message"));
    }
}

