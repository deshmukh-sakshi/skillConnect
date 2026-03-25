package com.skillconnect.backend.Service.freelancer;

import com.skillconnect.backend.Auth.Entity.AppUser;
import com.skillconnect.backend.DTO.FreelancerDTO;
import com.skillconnect.backend.Entity.Freelancer;
import com.skillconnect.backend.Entity.PastWork;
import com.skillconnect.backend.Entity.Skills;
import com.skillconnect.backend.Repository.FreelancerRepository;
import com.skillconnect.backend.Repository.PastWorkRepository;
import com.skillconnect.backend.Repository.SkillRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FreelancerServiceImplTest {

    @Mock
    private FreelancerRepository freelancerRepo;

    @Mock
    private SkillRepository skillRepo;

    @Mock
    private PastWorkRepository pastWorkRepo;

    @InjectMocks
    private FreelancerServiceImpl freelancerService;

    @Test
    void getFreelancerProfile_whenMissing_throwsRuntimeException() {
        when(freelancerRepo.findById(9L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> freelancerService.getFreelancerProfile(9L));

        assertEquals("Freelancer not found", ex.getMessage());
    }

    @Test
    void getFreelancerProfile_success_mapsProfileDetails() {
        AppUser appUser = new AppUser();
        appUser.setEmail("sam@example.com");

        Freelancer freelancer = new Freelancer();
        freelancer.setId(4L);
        freelancer.setName("Sam");
        freelancer.setRating(4.8);
        freelancer.setAppUser(appUser);

        Skills java = new Skills();
        java.setName("Java");
        Skills spring = new Skills();
        spring.setName("Spring");

        PastWork work = new PastWork();
        work.setTitle("Ecommerce");
        work.setLink("https://portfolio/ecommerce");
        work.setDescription("Built backend APIs");

        when(freelancerRepo.findById(4L)).thenReturn(Optional.of(freelancer));
        when(skillRepo.findByFreelancers_Id(4L)).thenReturn(List.of(java, spring));
        when(pastWorkRepo.findByFreelancerId(4L)).thenReturn(List.of(work));

        FreelancerDTO result = freelancerService.getFreelancerProfile(4L);

        assertEquals("Sam", result.getName());
        assertEquals("sam@example.com", result.getEmail());
        assertEquals(4.8, result.getRating());
        assertEquals(List.of("Java", "Spring"), result.getSkills());
        assertEquals(1, result.getPastWorks().size());
        assertEquals("Ecommerce", result.getPastWorks().getFirst().getTitle());
        assertEquals("https://portfolio/ecommerce", result.getPastWorks().getFirst().getLink());
        assertEquals("Built backend APIs", result.getPastWorks().getFirst().getDescription());
    }

    @Test
    void deleteFreelancer_deletesById() {
        freelancerService.deleteFreelancer(7L);

        verify(freelancerRepo).deleteById(7L);
    }
}


