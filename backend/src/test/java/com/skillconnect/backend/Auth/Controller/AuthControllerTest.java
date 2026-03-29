package com.skillconnect.backend.Auth.Controller;

import com.skillconnect.backend.Auth.DTO.AuthResponse;
import com.skillconnect.backend.Auth.DTO.LoginRequest;
import com.skillconnect.backend.Auth.DTO.RegistrationRequest;
import com.skillconnect.backend.Auth.Service.AuthService;
import com.skillconnect.backend.DTO.ApiResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @Test
    void login_returnsOkWithServicePayload() {
        LoginRequest request = new LoginRequest("user@test.com", "password123");
        ApiResponse<AuthResponse> payload = ApiResponse.success(
                AuthResponse.builder().email("user@test.com").token("jwt").role("ROLE_CLIENT").name("User").build()
        );

        when(authService.login(request)).thenReturn(payload);

        ResponseEntity<ApiResponse<AuthResponse>> response = authController.login(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("success", response.getBody().getStatus());
        assertNotNull(response.getBody().getData());
        assertEquals("user@test.com", response.getBody().getData().getEmail());
    }

    @Test
    void registerClient_returnsOkWithServicePayload() {
        RegistrationRequest request = new RegistrationRequest("Client", "client@test.com", "password123");
        ApiResponse<AuthResponse> payload = ApiResponse.success(
                AuthResponse.builder().email("client@test.com").token("jwt").role("ROLE_CLIENT").name("Client").build()
        );

        when(authService.registerClient(request)).thenReturn(payload);

        ResponseEntity<ApiResponse<AuthResponse>> response = authController.registerClient(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getData());
        assertEquals("ROLE_CLIENT", response.getBody().getData().getRole());
    }

    @Test
    void registerFreelancer_returnsOkWithServicePayload() {
        RegistrationRequest request = new RegistrationRequest("Freelancer", "freelancer@test.com", "password123");
        ApiResponse<AuthResponse> payload = ApiResponse.success(
                AuthResponse.builder().email("freelancer@test.com").token("jwt").role("ROLE_FREELANCER").name("Freelancer").build()
        );

        when(authService.registerFreelancer(request)).thenReturn(payload);

        ResponseEntity<ApiResponse<AuthResponse>> response = authController.registerFreelancer(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getData());
        assertEquals("ROLE_FREELANCER", response.getBody().getData().getRole());
    }
}


