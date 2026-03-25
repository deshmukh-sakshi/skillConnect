package com.skillconnect.backend.Service.pastWork;

import com.skillconnect.backend.DTO.PastWorkDTO;
import com.skillconnect.backend.Entity.Freelancer;
import com.skillconnect.backend.Entity.PastWork;
import com.skillconnect.backend.Repository.FreelancerRepository;
import com.skillconnect.backend.Repository.PastWorkRepository;
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
class PastWorkServiceImplTest {

    @Mock
    private PastWorkRepository pastWorkRepo;

    @Mock
    private FreelancerRepository freelancerRepo;

    @InjectMocks
    private PastWorkServiceImpl pastWorkService;

    @Test
    void addPastWork_whenFreelancerMissing_throwsRuntimeException() {
        PastWorkDTO dto = new PastWorkDTO("Project", "https://link", "desc", 10L);
        when(freelancerRepo.findById(10L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> pastWorkService.addPastWork(dto));

        assertEquals("Freelancer not found", ex.getMessage());
    }

    @Test
    void addPastWork_success_mapsSavedEntityToDto() {
        PastWorkDTO dto = new PastWorkDTO("Project", "https://link", "desc", 10L);

        Freelancer freelancer = new Freelancer();
        freelancer.setId(10L);

        when(freelancerRepo.findById(10L)).thenReturn(Optional.of(freelancer));
        when(pastWorkRepo.save(org.mockito.ArgumentMatchers.any(PastWork.class))).thenAnswer(invocation -> {
            PastWork saved = invocation.getArgument(0);
            saved.setId(55L);
            return saved;
        });

        PastWorkDTO result = pastWorkService.addPastWork(dto);

        assertEquals("Project", result.getTitle());
        assertEquals("https://link", result.getLink());
        assertEquals("desc", result.getDescription());
        assertEquals(10L, result.getFreelancerId());
    }

    @Test
    void getPastWorkByFreelancerId_returnsRepositoryResult() {
        PastWork work = new PastWork();
        work.setId(1L);
        when(pastWorkRepo.findByFreelancerId(4L)).thenReturn(List.of(work));

        List<PastWork> result = pastWorkService.getPastWorkByFreelancerId(4L);

        assertEquals(1, result.size());
        assertEquals(1L, result.getFirst().getId());
    }

    @Test
    void updatePastWork_whenMissing_throwsRuntimeException() {
        PastWorkDTO dto = new PastWorkDTO("Updated", "https://new", "new desc", 4L);
        when(pastWorkRepo.findById(9L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> pastWorkService.updatePastWork(9L, dto));

        assertEquals("Past work not found", ex.getMessage());
    }

    @Test
    void updatePastWork_success_updatesFieldsAndSaves() {
        PastWorkDTO dto = new PastWorkDTO("Updated", "https://new", "new desc", 4L);

        PastWork existing = new PastWork();
        existing.setId(9L);
        existing.setTitle("Old");
        existing.setLink("https://old");
        existing.setDescription("old desc");

        when(pastWorkRepo.findById(9L)).thenReturn(Optional.of(existing));
        when(pastWorkRepo.save(existing)).thenReturn(existing);

        PastWork result = pastWorkService.updatePastWork(9L, dto);

        assertEquals("Updated", result.getTitle());
        assertEquals("https://new", result.getLink());
        assertEquals("new desc", result.getDescription());
        verify(pastWorkRepo).save(existing);
    }

    @Test
    void deletePastWork_deletesById() {
        pastWorkService.deletePastWork(7L);

        verify(pastWorkRepo).deleteById(7L);
    }
}

