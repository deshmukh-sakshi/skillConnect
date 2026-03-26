package com.skillconnect.backend.Service.Skills;

import com.skillconnect.backend.Auth.Entity.AppUser;
import com.skillconnect.backend.DTO.FreelancerDTO;
import com.skillconnect.backend.DTO.FreelancerUpdateDTO;
import com.skillconnect.backend.Entity.Freelancer;
import com.skillconnect.backend.Entity.Skills;
import com.skillconnect.backend.Repository.FreelancerRepository;
import com.skillconnect.backend.Repository.PastWorkRepository;
import com.skillconnect.backend.Repository.SkillRepository;
import com.skillconnect.backend.Service.freelancer.FreelancerServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SkillsServiceImplTest {

    @Mock
    private SkillRepository skillRepo;

    @Mock
    private FreelancerRepository freelancerRepo;

    @Mock
    private PastWorkRepository pastWorkRepo;

    @InjectMocks
    private FreelancerServiceImpl freelancerService;

    private Freelancer buildFreelancer(Long id) {
        AppUser appUser = new AppUser();
        appUser.setEmail("test@example.com");
        Freelancer freelancer = new Freelancer();
        freelancer.setId(id);
        freelancer.setName("Test");
        freelancer.setRating(4.0);
        freelancer.setAppUser(appUser);
        return freelancer;
    }

    @Test
    void updateSkills_whenFreelancerMissing_throwsRuntimeException() {
        FreelancerUpdateDTO dto = new FreelancerUpdateDTO();
        dto.setName("Test");
        dto.setSkills(List.of("Java"));
        when(freelancerRepo.findById(10L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> freelancerService.updateFreelancerProfile(10L, dto));

        assertEquals("Freelancer not found", ex.getMessage());
        verify(skillRepo, never()).findByNameIgnoreCase(any());
    }

    @Test
    void updateSkills_whenSkillAlreadyExists_reusesSkillWithoutCreating() {
        Freelancer freelancer = buildFreelancer(10L);
        Skills existing = new Skills();
        existing.setId(3L);
        existing.setName("Java");

        FreelancerUpdateDTO dto = new FreelancerUpdateDTO();
        dto.setName("Test");
        dto.setSkills(List.of("Java"));

        when(freelancerRepo.findById(10L)).thenReturn(Optional.of(freelancer));
        when(skillRepo.findByNameIgnoreCase("Java")).thenReturn(existing);
        when(freelancerRepo.save(freelancer)).thenReturn(freelancer);
        when(skillRepo.findByFreelancers_Id(10L)).thenReturn(List.of(existing));
        when(pastWorkRepo.findByFreelancerId(10L)).thenReturn(List.of());

        FreelancerDTO result = freelancerService.updateFreelancerProfile(10L, dto);

        verify(skillRepo, never()).save(any(Skills.class));
        verify(freelancerRepo).save(freelancer);
        assertEquals(List.of("Java"), result.getSkills());
    }

    @Test
    void updateSkills_whenNewSkill_createsAndAddsToFreelancer() {
        Freelancer freelancer = buildFreelancer(12L);
        Skills newSkill = new Skills();
        newSkill.setId(55L);
        newSkill.setName("Go");

        FreelancerUpdateDTO dto = new FreelancerUpdateDTO();
        dto.setName("Test");
        dto.setSkills(List.of("Go"));

        when(freelancerRepo.findById(12L)).thenReturn(Optional.of(freelancer));
        when(skillRepo.findByNameIgnoreCase("Go")).thenReturn(null);
        when(skillRepo.save(any(Skills.class))).thenReturn(newSkill);
        when(freelancerRepo.save(freelancer)).thenReturn(freelancer);
        when(skillRepo.findByFreelancers_Id(12L)).thenReturn(List.of(newSkill));
        when(pastWorkRepo.findByFreelancerId(12L)).thenReturn(List.of());

        FreelancerDTO result = freelancerService.updateFreelancerProfile(12L, dto);

        verify(skillRepo).save(any(Skills.class));
        verify(freelancerRepo).save(freelancer);
        assertEquals(List.of("Go"), result.getSkills());
    }

    @Test
    void updateSkills_withEmptyList_clearsAllSkillsFromFreelancer() {
        Freelancer freelancer = buildFreelancer(7L);
        Skills java = new Skills();
        java.setId(9L);
        java.setName("Java");
        freelancer.getFreelancerSkill().add(java);

        FreelancerUpdateDTO dto = new FreelancerUpdateDTO();
        dto.setName("Test");
        dto.setSkills(List.of());

        when(freelancerRepo.findById(7L)).thenReturn(Optional.of(freelancer));
        when(freelancerRepo.save(freelancer)).thenReturn(freelancer);
        when(skillRepo.findByFreelancers_Id(7L)).thenReturn(List.of());
        when(pastWorkRepo.findByFreelancerId(7L)).thenReturn(List.of());

        FreelancerDTO result = freelancerService.updateFreelancerProfile(7L, dto);

        verify(freelancerRepo).save(freelancer);
        assertTrue(freelancer.getFreelancerSkill().isEmpty());
        assertTrue(result.getSkills().isEmpty());
    }
}
