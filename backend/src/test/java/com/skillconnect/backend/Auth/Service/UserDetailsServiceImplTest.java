package com.skillconnect.backend.Auth.Service;

import com.skillconnect.backend.Auth.Entity.AppUser;
import com.skillconnect.backend.Auth.Entity.Role;
import com.skillconnect.backend.Auth.Repository.AppUserRepository;
import com.skillconnect.backend.Entity.Client;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @Mock
    private AppUserRepository appUserRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Test
    void loadUserByUsername_whenFound_returnsUserDetails() {
        AppUser user = new AppUser();
        user.setEmail("client@test.com");
        user.setPassword("encoded");
        user.setRole(Role.ROLE_CLIENT);

        Client client = new Client();
        client.setName("Client Name");
        user.setClientProfile(client);

        when(appUserRepository.findByEmail("client@test.com")).thenReturn(Optional.of(user));

        UserDetails details = userDetailsService.loadUserByUsername("client@test.com");

        assertEquals("client@test.com", details.getUsername());
        assertEquals("encoded", details.getPassword());
        assertEquals("ROLE_CLIENT", details.getAuthorities().iterator().next().getAuthority());
    }

    @Test
    void loadUserByUsername_whenMissing_throwsUsernameNotFoundException() {
        when(appUserRepository.findByEmail("missing@test.com")).thenReturn(Optional.empty());

        UsernameNotFoundException ex = assertThrows(
                UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername("missing@test.com")
        );

        assertTrue(ex.getMessage().contains("user not found"));
    }
}

