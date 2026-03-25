package com.skillconnect.backend.Service.Skills;

import com.skillconnect.backend.DTO.SkillDTO;
import com.skillconnect.backend.Entity.Freelancer;
import com.skillconnect.backend.Entity.Skills;
import com.skillconnect.backend.Repository.FreelancerRepository;
import com.skillconnect.backend.Repository.SkillRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SkillsServiceImplTest {

    @Mock
    private SkillRepository skillRepo;

    @Mock
    private FreelancerRepository freelancerRepo;

    @InjectMocks
    private SkillsServiceImpl skillsService;

    @Test
    void addSkill_whenFreelancerMissing_throwsRuntimeException() {
        SkillDTO dto = new SkillDTO(null, "Java", 10L);
        when(freelancerRepo.findById(10L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> skillsService.addSkill(dto));

        assertEquals("Freelancer not found", ex.getMessage());
        verify(skillRepo, never()).findByNameIgnoreCase(any());
    }

    @Test
    void addSkill_whenSkillAlreadyExists_reusesSkillAndSavesRelation() {
        SkillDTO dto = new SkillDTO(null, "Java", 10L);

        Freelancer freelancer = new Freelancer();
        freelancer.setId(10L);

        Skills skill = new Skills();
        skill.setId(3L);
        skill.setName("Java");

        when(freelancerRepo.findById(10L)).thenReturn(Optional.of(freelancer));
        when(skillRepo.findByNameIgnoreCase("Java")).thenReturn(skill);

        SkillDTO result = skillsService.addSkill(dto);

        assertEquals(3L, result.getId());
        assertEquals("Java", result.getSkillName());
        assertEquals(10L, result.getFreelancerId());
        verify(skillRepo, never()).save(any(Skills.class));
        verify(freelancerRepo).save(freelancer);
    }

    @Test
    void addSkill_whenSkillMissing_createsSkillAndSavesRelation() {
        SkillDTO dto = new SkillDTO(null, "Go", 12L);

        Freelancer freelancer = new Freelancer();
        freelancer.setId(12L);

        Skills newSkill = new Skills();
        newSkill.setName("Go");

        when(freelancerRepo.findById(12L)).thenReturn(Optional.of(freelancer));
        when(skillRepo.findByNameIgnoreCase("Go")).thenReturn(null);
        when(skillRepo.save(any(Skills.class))).thenAnswer(invocation -> {
            Skills s = invocation.getArgument(0);
            s.setId(55L);
            return s;
        });

        SkillDTO result = skillsService.addSkill(dto);

        assertEquals(55L, result.getId());
        assertEquals("Go", result.getSkillName());
        assertEquals(12L, result.getFreelancerId());
        verify(skillRepo).save(any(Skills.class));
        verify(freelancerRepo).save(freelancer);
    }

    @Test
    void removeSkillFromFreelancer_whenSkillMissing_throwsRuntimeException() {
        when(skillRepo.findByNameIgnoreCase("Rust")).thenReturn(null);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> skillsService.removeSkillFromFreelancer(7L, "Rust"));

        assertEquals("Skill not found", ex.getMessage());
        verify(freelancerRepo, never()).findById(any());
    }

    @Test
    void removeSkillFromFreelancer_whenFreelancerMissing_throwsRuntimeException() {
        Skills skill = new Skills();
        skill.setName("Java");

        when(skillRepo.findByNameIgnoreCase("Java")).thenReturn(skill);
        when(freelancerRepo.findById(7L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> skillsService.removeSkillFromFreelancer(7L, "Java"));

        assertEquals("Freelancer not found", ex.getMessage());
    }

    @Test
    void removeSkillFromFreelancer_success_updatesRelationAndSavesFreelancer() {
        Freelancer freelancer = new Freelancer();
        freelancer.setId(7L);

        Skills skill = new Skills();
        skill.setId(9L);
        skill.setName("Java");

        freelancer.getFreelancerSkill().add(skill);
        skill.getFreelancers().add(freelancer);

        when(skillRepo.findByNameIgnoreCase("Java")).thenReturn(skill);
        when(freelancerRepo.findById(7L)).thenReturn(Optional.of(freelancer));

        skillsService.removeSkillFromFreelancer(7L, "Java");

        verify(freelancerRepo).save(freelancer);
        assertEquals(0, freelancer.getFreelancerSkill().size());
        assertEquals(0, skill.getFreelancers().size());
    }
}

