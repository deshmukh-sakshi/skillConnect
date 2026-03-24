package com.skillconnect.backend.Auth.Service;

import com.skillconnect.backend.Auth.DTO.AppUserDetails;
import com.skillconnect.backend.Auth.DTO.AuthResponse;
import com.skillconnect.backend.Auth.DTO.LoginRequest;
import com.skillconnect.backend.Auth.DTO.RegistrationRequest;
import com.skillconnect.backend.Auth.Entity.AppUser;
import com.skillconnect.backend.Auth.Entity.Role;
import com.skillconnect.backend.Auth.Repository.AppUserRepository;
import com.skillconnect.backend.DTO.ApiResponse;
import com.skillconnect.backend.Entity.Client;
import com.skillconnect.backend.Entity.Freelancer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private AppUserRepository appUserRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    @Test
    void login_success_returnsAuthResponseEnvelope() {
        AppUser appUser = new AppUser();
        appUser.setEmail("client@test.com");
        appUser.setPassword("encoded");
        appUser.setRole(Role.ROLE_CLIENT);

        Client profile = new Client();
        profile.setName("Client One");
        appUser.setClientProfile(profile);

        AppUserDetails userDetails = new AppUserDetails(appUser);
        Authentication authenticated = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authenticated);
        when(jwtService.generateToken(userDetails)).thenReturn("jwt-token");

        LoginRequest request = new LoginRequest("client@test.com", "password123");

        ApiResponse<AuthResponse> response = authService.login(request);

        assertEquals("success", response.getStatus());
        assertNotNull(response.getData());
        assertEquals("Client One", response.getData().getName());
        assertEquals("client@test.com", response.getData().getEmail());
        assertEquals("ROLE_CLIENT", response.getData().getRole());
        assertEquals("jwt-token", response.getData().getToken());
    }

    @Test
    void registerClient_existingEmail_throwsRuntimeException() {
        RegistrationRequest request = new RegistrationRequest("Client", "client@test.com", "password123");

        when(appUserRepository.findByEmail("client@test.com")).thenReturn(Optional.of(new AppUser()));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> authService.registerClient(request));

        assertEquals("user already exists", ex.getMessage());
        verify(appUserRepository, never()).save(any(AppUser.class));
    }

    @Test
    void registerClient_success_createsClientProfileAndReturnsToken() {
        RegistrationRequest request = new RegistrationRequest("Client Name", "newclient@test.com", "password123");

        when(appUserRepository.findByEmail("newclient@test.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password123")).thenReturn("encoded-password");
        when(appUserRepository.save(any(AppUser.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(jwtService.generateToken(any(AppUserDetails.class))).thenReturn("token-client");

        ApiResponse<AuthResponse> response = authService.registerClient(request);

        assertEquals("success", response.getStatus());
        assertNotNull(response.getData());
        assertEquals("Client Name", response.getData().getName());
        assertEquals("newclient@test.com", response.getData().getEmail());
        assertEquals("ROLE_CLIENT", response.getData().getRole());
        assertEquals("token-client", response.getData().getToken());

        ArgumentCaptor<AppUser> captor = ArgumentCaptor.forClass(AppUser.class);
        verify(appUserRepository).save(captor.capture());
        AppUser saved = captor.getValue();

        assertEquals("newclient@test.com", saved.getEmail());
        assertEquals("encoded-password", saved.getPassword());
        assertEquals(Role.ROLE_CLIENT, saved.getRole());
        assertNotNull(saved.getClientProfile());
        assertNull(saved.getFreelancerProfile());
        assertEquals("Client Name", saved.getClientProfile().getName());
    }

    @Test
    void registerFreelancer_success_createsFreelancerProfileAndReturnsToken() {
        RegistrationRequest request = new RegistrationRequest("Freelancer Name", "freelancer@test.com", "password123");

        when(appUserRepository.findByEmail("freelancer@test.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password123")).thenReturn("encoded-password");
        when(appUserRepository.save(any(AppUser.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(jwtService.generateToken(any(AppUserDetails.class))).thenReturn("token-freelancer");

        ApiResponse<AuthResponse> response = authService.registerFreelancer(request);

        assertEquals("success", response.getStatus());
        assertNotNull(response.getData());
        assertEquals("Freelancer Name", response.getData().getName());
        assertEquals("freelancer@test.com", response.getData().getEmail());
        assertEquals("ROLE_FREELANCER", response.getData().getRole());
        assertEquals("token-freelancer", response.getData().getToken());

        ArgumentCaptor<AppUser> captor = ArgumentCaptor.forClass(AppUser.class);
        verify(appUserRepository).save(captor.capture());
        AppUser saved = captor.getValue();

        assertEquals(Role.ROLE_FREELANCER, saved.getRole());
        assertNull(saved.getClientProfile());
        assertNotNull(saved.getFreelancerProfile());

        Freelancer freelancerProfile = saved.getFreelancerProfile();
        assertEquals("Freelancer Name", freelancerProfile.getName());
    }
}

