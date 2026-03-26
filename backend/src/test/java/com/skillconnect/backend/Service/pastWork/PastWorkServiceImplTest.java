package com.skillconnect.backend.Service.pastWork;

import com.skillconnect.backend.Auth.Entity.AppUser;
import com.skillconnect.backend.DTO.FreelancerDTO;
import com.skillconnect.backend.DTO.FreelancerUpdateDTO;
import com.skillconnect.backend.DTO.PastWorkUpdateDTO;
import com.skillconnect.backend.Entity.Freelancer;
import com.skillconnect.backend.Entity.PastWork;
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
class PastWorkServiceImplTest {

    @Mock
    private PastWorkRepository pastWorkRepo;

    @Mock
    private FreelancerRepository freelancerRepo;

    @Mock
    private SkillRepository skillRepo;

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

    private PastWorkUpdateDTO buildPastWorkDto(Long id, String title, String link, String desc, Boolean toDelete) {
        PastWorkUpdateDTO dto = new PastWorkUpdateDTO();
        dto.setId(id);
        dto.setTitle(title);
        dto.setLink(link);
        dto.setDescription(desc);
        dto.setToDelete(toDelete);
        return dto;
    }

    @Test
    void updatePastWorks_whenFreelancerMissing_throwsRuntimeException() {
        FreelancerUpdateDTO dto = new FreelancerUpdateDTO();
        dto.setName("Test");
        dto.setPastWorks(List.of(buildPastWorkDto(null, "Project", "https://link", "desc", null)));
        when(freelancerRepo.findById(10L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> freelancerService.updateFreelancerProfile(10L, dto));

        assertEquals("Freelancer not found", ex.getMessage());
    }

    @Test
    void updatePastWorks_addNewPastWork_savesViaFreelancerCascade() {
        Freelancer freelancer = buildFreelancer(10L);
        PastWorkUpdateDTO pwDto = buildPastWorkDto(null, "Project", "https://link", "desc", null);

        FreelancerUpdateDTO dto = new FreelancerUpdateDTO();
        dto.setName("Test");
        dto.setPastWorks(List.of(pwDto));

        when(freelancerRepo.findById(10L)).thenReturn(Optional.of(freelancer));
        when(freelancerRepo.save(freelancer)).thenReturn(freelancer);
        when(skillRepo.findByFreelancers_Id(10L)).thenReturn(List.of());
        when(pastWorkRepo.findByFreelancerId(10L)).thenReturn(List.of());

        freelancerService.updateFreelancerProfile(10L, dto);

        verify(freelancerRepo).save(freelancer);
        assertEquals(1, freelancer.getPastWorks().size());
        assertEquals("Project", freelancer.getPastWorks().getFirst().getTitle());
    }

    @Test
    void updatePastWorks_withDeleteFlag_deletesFromRepository() {
        Freelancer freelancer = buildFreelancer(10L);
        PastWorkUpdateDTO pwDto = buildPastWorkDto(5L, "Old", "https://old", "old desc", true);

        FreelancerUpdateDTO dto = new FreelancerUpdateDTO();
        dto.setName("Test");
        dto.setPastWorks(List.of(pwDto));

        when(freelancerRepo.findById(10L)).thenReturn(Optional.of(freelancer));
        when(freelancerRepo.save(freelancer)).thenReturn(freelancer);
        when(skillRepo.findByFreelancers_Id(10L)).thenReturn(List.of());
        when(pastWorkRepo.findByFreelancerId(10L)).thenReturn(List.of());

        freelancerService.updateFreelancerProfile(10L, dto);

        verify(pastWorkRepo).deleteAllByIdInBatch(List.of(5L));
        assertTrue(freelancer.getPastWorks().isEmpty());
    }

    @Test
    void updatePastWorks_withExistingId_updatesFields() {
        Freelancer freelancer = buildFreelancer(10L);
        PastWork existing = new PastWork();
        existing.setId(9L);
        existing.setTitle("Old");
        existing.setLink("https://old");
        existing.setDescription("old desc");

        PastWorkUpdateDTO pwDto = buildPastWorkDto(9L, "Updated", "https://new", "new desc", null);

        FreelancerUpdateDTO dto = new FreelancerUpdateDTO();
        dto.setName("Test");
        dto.setPastWorks(List.of(pwDto));

        when(freelancerRepo.findById(10L)).thenReturn(Optional.of(freelancer));
        when(pastWorkRepo.findById(9L)).thenReturn(Optional.of(existing));
        when(freelancerRepo.save(freelancer)).thenReturn(freelancer);
        when(skillRepo.findByFreelancers_Id(10L)).thenReturn(List.of());

        PastWork savedWork = new PastWork();
        savedWork.setId(9L);
        savedWork.setTitle("Updated");
        when(pastWorkRepo.findByFreelancerId(10L)).thenReturn(List.of(savedWork));

        FreelancerDTO result = freelancerService.updateFreelancerProfile(10L, dto);

        verify(freelancerRepo).save(freelancer);
        assertEquals("Updated", existing.getTitle());
        assertEquals("https://new", existing.getLink());
        assertEquals("new desc", existing.getDescription());
        assertEquals(1, result.getPastWorks().size());
    }

    @Test
    void updatePastWorks_withNullPastWorksList_skipsProcessing() {
        Freelancer freelancer = buildFreelancer(10L);

        FreelancerUpdateDTO dto = new FreelancerUpdateDTO();
        dto.setName("Test");
        dto.setPastWorks(null);

        when(freelancerRepo.findById(10L)).thenReturn(Optional.of(freelancer));
        when(freelancerRepo.save(freelancer)).thenReturn(freelancer);
        when(skillRepo.findByFreelancers_Id(10L)).thenReturn(List.of());
        when(pastWorkRepo.findByFreelancerId(10L)).thenReturn(List.of());

        freelancerService.updateFreelancerProfile(10L, dto);

        verify(pastWorkRepo, never()).deleteAllByIdInBatch(any());
        verify(pastWorkRepo, never()).findById(any());
    }
}
