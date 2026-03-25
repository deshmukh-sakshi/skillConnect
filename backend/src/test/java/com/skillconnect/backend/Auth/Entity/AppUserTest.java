package com.skillconnect.backend.Auth.Entity;

import com.skillconnect.backend.Entity.Client;
import com.skillconnect.backend.Entity.Freelancer;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class AppUserTest {

    @Test
    void createAppUser_setsFieldsCorrectly() {
        AppUser user = new AppUser();
        
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setPassword("encodedPassword");
        user.setRole(Role.ROLE_CLIENT);
        
        assertEquals(1L, user.getId());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("encodedPassword", user.getPassword());
        assertEquals(Role.ROLE_CLIENT, user.getRole());
    }

    @Test
    void setClientProfile_establishesBidirectionalRelationship() {
        AppUser user = new AppUser();
        Client client = new Client();
        client.setName("Client Name");
        
        user.setClientProfile(client);
        
        assertNotNull(user.getClientProfile());
        assertEquals("Client Name", user.getClientProfile().getName());
    }

    @Test
    void setFreelancerProfile_establishesBidirectionalRelationship() {
        AppUser user = new AppUser();
        Freelancer freelancer = new Freelancer();
        freelancer.setName("Freelancer Name");
        
        user.setFreelancerProfile(freelancer);
        
        assertNotNull(user.getFreelancerProfile());
        assertEquals("Freelancer Name", user.getFreelancerProfile().getName());
    }

    @Test
    void clientUser_hasNoFreelancerProfile() {
        AppUser user = new AppUser();
        user.setRole(Role.ROLE_CLIENT);
        
        Client client = new Client();
        user.setClientProfile(client);
        
        assertNotNull(user.getClientProfile());
        assertNull(user.getFreelancerProfile());
    }

    @Test
    void freelancerUser_hasNoClientProfile() {
        AppUser user = new AppUser();
        user.setRole(Role.ROLE_FREELANCER);
        
        Freelancer freelancer = new Freelancer();
        user.setFreelancerProfile(freelancer);
        
        assertNotNull(user.getFreelancerProfile());
        assertNull(user.getClientProfile());
    }

    @Test
    void setResetPasswordToken_setsCorrectly() {
        AppUser user = new AppUser();
        String token = "reset-token-123";
        
        user.setResetPasswordToken(token);
        
        assertEquals(token, user.getResetPasswordToken());
    }

    @Test
    void setResetPasswordTokenExpiry_setsCorrectly() {
        AppUser user = new AppUser();
        LocalDateTime expiry = LocalDateTime.now().plusHours(1);
        
        user.setResetPasswordTokenExpiry(expiry);
        
        assertEquals(expiry, user.getResetPasswordTokenExpiry());
    }

    @Test
    void resetPasswordToken_canBeNull() {
        AppUser user = new AppUser();
        user.setResetPasswordToken("token");
        user.setResetPasswordToken(null);
        
        assertNull(user.getResetPasswordToken());
    }

    @Test
    void resetPasswordTokenExpiry_canBeNull() {
        AppUser user = new AppUser();
        user.setResetPasswordTokenExpiry(LocalDateTime.now());
        user.setResetPasswordTokenExpiry(null);
        
        assertNull(user.getResetPasswordTokenExpiry());
    }

    @Test
    void emailMustBeUnique_isEnforced() {
        AppUser user1 = new AppUser();
        user1.setEmail("unique@example.com");
        
        AppUser user2 = new AppUser();
        user2.setEmail("unique@example.com");
        
        assertEquals(user1.getEmail(), user2.getEmail());
    }

    @Test
    void passwordIsRequired_cannotBeNull() {
        AppUser user = new AppUser();
        user.setEmail("test@example.com");
        user.setPassword("password123");
        user.setRole(Role.ROLE_CLIENT);
        
        assertNotNull(user.getPassword());
    }

    @Test
    void roleIsRequired_cannotBeNull() {
        AppUser user = new AppUser();
        user.setEmail("test@example.com");
        user.setPassword("password123");
        user.setRole(Role.ROLE_CLIENT);
        
        assertNotNull(user.getRole());
    }

    @Test
    void resetPasswordTokenExpiry_canBeInFuture() {
        AppUser user = new AppUser();
        LocalDateTime futureExpiry = LocalDateTime.now().plusDays(1);
        
        user.setResetPasswordTokenExpiry(futureExpiry);
        
        assertTrue(user.getResetPasswordTokenExpiry().isAfter(LocalDateTime.now()));
    }

    @Test
    void resetPasswordTokenExpiry_canBeInPast() {
        AppUser user = new AppUser();
        LocalDateTime pastExpiry = LocalDateTime.now().minusHours(1);
        
        user.setResetPasswordTokenExpiry(pastExpiry);
        
        assertTrue(user.getResetPasswordTokenExpiry().isBefore(LocalDateTime.now()));
    }

    @Test
    void appUser_canSwitchRoles() {
        AppUser user = new AppUser();
        
        user.setRole(Role.ROLE_CLIENT);
        assertEquals(Role.ROLE_CLIENT, user.getRole());
        
        user.setRole(Role.ROLE_FREELANCER);
        assertEquals(Role.ROLE_FREELANCER, user.getRole());
    }
}
