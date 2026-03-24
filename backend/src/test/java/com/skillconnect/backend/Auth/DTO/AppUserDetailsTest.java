package com.skillconnect.backend.Auth.DTO;

import com.skillconnect.backend.Auth.Entity.AppUser;
import com.skillconnect.backend.Auth.Entity.Role;
import com.skillconnect.backend.Entity.Client;
import com.skillconnect.backend.Entity.Freelancer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AppUserDetailsTest {

    @Test
    void constructor_withClientRole_mapsClientNameAndAuthority() {
        AppUser user = new AppUser();
        user.setEmail("client@test.com");
        user.setPassword("pwd");
        user.setRole(Role.ROLE_CLIENT);

        Client client = new Client();
        client.setName("Client A");
        user.setClientProfile(client);

        AppUserDetails details = new AppUserDetails(user);

        assertEquals("client@test.com", details.getUsername());
        assertEquals("pwd", details.getPassword());
        assertEquals("Client A", details.getName());
        assertEquals("ROLE_CLIENT", details.getAuthorities().iterator().next().getAuthority());
    }

    @Test
    void constructor_withFreelancerRole_mapsFreelancerNameAndAuthority() {
        AppUser user = new AppUser();
        user.setEmail("freelancer@test.com");
        user.setPassword("pwd");
        user.setRole(Role.ROLE_FREELANCER);

        Freelancer freelancer = new Freelancer();
        freelancer.setName("Freelancer A");
        user.setFreelancerProfile(freelancer);

        AppUserDetails details = new AppUserDetails(user);

        assertEquals("Freelancer A", details.getName());
        assertEquals("ROLE_FREELANCER", details.getAuthorities().iterator().next().getAuthority());
    }
}

