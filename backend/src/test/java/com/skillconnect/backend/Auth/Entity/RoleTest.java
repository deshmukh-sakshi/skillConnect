package com.skillconnect.backend.Auth.Entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoleTest {

    @Test
    void roleEnum_hasClientRole() {
        Role role = Role.ROLE_CLIENT;
        
        assertNotNull(role);
        assertEquals("ROLE_CLIENT", role.name());
    }

    @Test
    void roleEnum_hasFreelancerRole() {
        Role role = Role.ROLE_FREELANCER;
        
        assertNotNull(role);
        assertEquals("ROLE_FREELANCER", role.name());
    }

    @Test
    void roleEnum_hasTwoValues() {
        Role[] roles = Role.values();
        
        assertEquals(2, roles.length);
    }

    @Test
    void roleEnum_containsExpectedValues() {
        Role[] roles = Role.values();
        
        assertTrue(containsRole(roles, Role.ROLE_CLIENT));
        assertTrue(containsRole(roles, Role.ROLE_FREELANCER));
    }

    @Test
    void valueOf_returnsCorrectRole() {
        Role clientRole = Role.valueOf("ROLE_CLIENT");
        Role freelancerRole = Role.valueOf("ROLE_FREELANCER");
        
        assertEquals(Role.ROLE_CLIENT, clientRole);
        assertEquals(Role.ROLE_FREELANCER, freelancerRole);
    }

    @Test
    void valueOf_withInvalidRole_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            Role.valueOf("ROLE_ADMIN");
        });
    }

    @Test
    void roleComparison_worksCorrectly() {
        Role role1 = Role.ROLE_CLIENT;
        Role role2 = Role.ROLE_CLIENT;
        Role role3 = Role.ROLE_FREELANCER;
        
        assertEquals(role1, role2);
        assertNotEquals(role1, role3);
    }

    @Test
    void roleToString_returnsRoleName() {
        assertEquals("ROLE_CLIENT", Role.ROLE_CLIENT.toString());
        assertEquals("ROLE_FREELANCER", Role.ROLE_FREELANCER.toString());
    }

    @Test
    void roleOrdinal_isConsistent() {
        assertEquals(0, Role.ROLE_CLIENT.ordinal());
        assertEquals(1, Role.ROLE_FREELANCER.ordinal());
    }

    @Test
    void roleName_startsWithROLE() {
        for (Role role : Role.values()) {
            assertTrue(role.name().startsWith("ROLE_"));
        }
    }

    private boolean containsRole(Role[] roles, Role targetRole) {
        for (Role role : roles) {
            if (role == targetRole) {
                return true;
            }
        }
        return false;
    }
}
