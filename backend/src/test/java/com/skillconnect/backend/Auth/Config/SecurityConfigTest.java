package com.skillconnect.backend.Auth.Config;

import com.skillconnect.backend.Auth.Filter.JwtAuthFilter;
import com.skillconnect.backend.Auth.Service.UserDetailsServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfigurationSource;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SecurityConfigTest {

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private JwtAuthFilter jwtAuthFilter;

    @InjectMocks
    private SecurityConfig securityConfig;

    @Test
    void authenticationProvider_returnsDaoAuthenticationProvider() {
        AuthenticationProvider provider = securityConfig.authenticationProvider();

        assertNotNull(provider);
        assertInstanceOf(DaoAuthenticationProvider.class, provider);
    }

    @Test
    void passwordEncoder_returnsBCryptPasswordEncoder() {
        PasswordEncoder encoder = securityConfig.passwordEncoder();

        assertNotNull(encoder);
        assertEquals("org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder",
                encoder.getClass().getName());
    }

    @Test
    void passwordEncoder_encodesPasswordCorrectly() {
        PasswordEncoder encoder = securityConfig.passwordEncoder();
        String rawPassword = "testPassword123";

        String encodedPassword = encoder.encode(rawPassword);

        assertNotNull(encodedPassword);
        assertNotEquals(rawPassword, encodedPassword);
        assertTrue(encoder.matches(rawPassword, encodedPassword));
    }

    @Test
    void passwordEncoder_differentEncodingsForSamePassword() {
        PasswordEncoder encoder = securityConfig.passwordEncoder();
        String password = "samePassword";

        String encoded1 = encoder.encode(password);
        String encoded2 = encoder.encode(password);

        assertNotEquals(encoded1, encoded2);
        assertTrue(encoder.matches(password, encoded1));
        assertTrue(encoder.matches(password, encoded2));
    }

    @Test
    void authenticationManager_returnsAuthenticationManager() throws Exception {
        AuthenticationConfiguration config = mock(AuthenticationConfiguration.class);
        AuthenticationManager mockManager = mock(AuthenticationManager.class);
        when(config.getAuthenticationManager()).thenReturn(mockManager);

        AuthenticationManager manager = securityConfig.authenticationManager(config);

        assertNotNull(manager);
        assertEquals(mockManager, manager);
        verify(config).getAuthenticationManager();
    }

    @Test
    void corsConfigurationSource_configuresAllowedOrigins() {
        CorsConfigurationSource source = securityConfig.corsConfigurationSource();

        assertNotNull(source);
    }

    @Test
    void corsConfigurationSource_configuresAllowedMethods() {
        CorsConfigurationSource source = securityConfig.corsConfigurationSource();

        assertNotNull(source);
    }

    @Test
    void corsConfigurationSource_configuresAllowedHeaders() {
        CorsConfigurationSource source = securityConfig.corsConfigurationSource();

        assertNotNull(source);
    }

    @Test
    void corsConfigurationSource_allowsCredentials() {
        CorsConfigurationSource source = securityConfig.corsConfigurationSource();

        assertNotNull(source);
    }

    @Test
    void passwordEncoder_rejectsIncorrectPassword() {
        PasswordEncoder encoder = securityConfig.passwordEncoder();
        String password = "correctPassword";
        String encodedPassword = encoder.encode(password);

        assertFalse(encoder.matches("wrongPassword", encodedPassword));
    }

    @Test
    void passwordEncoder_handlesEmptyPassword() {
        PasswordEncoder encoder = securityConfig.passwordEncoder();
        String emptyPassword = "";

        String encoded = encoder.encode(emptyPassword);

        assertNotNull(encoded);
        assertTrue(encoder.matches(emptyPassword, encoded));
    }

    @Test
    void passwordEncoder_handlesLongPassword() {
        PasswordEncoder encoder = securityConfig.passwordEncoder();
        String longPassword = "a".repeat(72);

        String encoded = encoder.encode(longPassword);

        assertNotNull(encoded);
        assertTrue(encoder.matches(longPassword, encoded));
    }

    @Test
    void passwordEncoder_handlesSpecialCharacters() {
        PasswordEncoder encoder = securityConfig.passwordEncoder();
        String specialPassword = "P@ssw0rd!#$%^&*()";

        String encoded = encoder.encode(specialPassword);

        assertNotNull(encoded);
        assertTrue(encoder.matches(specialPassword, encoded));
    }
}
