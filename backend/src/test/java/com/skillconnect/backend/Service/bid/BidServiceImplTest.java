package com.skillconnect.backend.Service.bid;

import com.skillconnect.backend.DTO.BidDTO;
import com.skillconnect.backend.DTO.BidResponseDTO;
import com.skillconnect.backend.Entity.Bids;
import com.skillconnect.backend.Entity.Freelancer;
import com.skillconnect.backend.Entity.Project;
import com.skillconnect.backend.Repository.BidRepository;
import com.skillconnect.backend.Repository.FreelancerRepository;
import com.skillconnect.backend.Repository.ProjectRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BidServiceImplTest {

    @Mock
    private BidRepository bidRepo;

    @Mock
    private FreelancerRepository freelancerRepo;

    @Mock
    private ProjectRepository projectRepo;

    @InjectMocks
    private BidServiceImpl bidService;

    @Test
    void placeBid_whenFreelancerMissing_throwsRuntimeException() {
        BidDTO dto = new BidDTO(9L, 3L, "Proposal", 3000.0, 10L, null);
        when(freelancerRepo.findById(9L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> bidService.placeBid(dto));

        assertEquals("Freelancer not found", ex.getMessage());
        verify(projectRepo, never()).findById(any());
    }

    @Test
    void placeBid_whenProjectMissing_throwsRuntimeException() {
        BidDTO dto = new BidDTO(1L, 3L, "Proposal", 3000.0, 10L, null);

        Freelancer freelancer = new Freelancer();
        freelancer.setId(1L);

        when(freelancerRepo.findById(1L)).thenReturn(Optional.of(freelancer));
        when(projectRepo.findById(3L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> bidService.placeBid(dto));

        assertEquals("Project not found", ex.getMessage());
    }

    @Test
    void placeBid_whenProjectClosed_throwsIllegalStateException() {
        BidDTO dto = new BidDTO(1L, 3L, "Proposal", 3000.0, 10L, null);

        Freelancer freelancer = new Freelancer();
        freelancer.setId(1L);

        Project project = new Project();
        project.setId(3L);
        project.setStatus(Project.ProjectStatus.CLOSED);

        when(freelancerRepo.findById(1L)).thenReturn(Optional.of(freelancer));
        when(projectRepo.findById(3L)).thenReturn(Optional.of(project));

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> bidService.placeBid(dto));

        assertEquals("Bids can only be placed on projects with status OPEN", ex.getMessage());
    }

    @Test
    void placeBid_whenAlreadyBid_throwsIllegalStateException() {
        BidDTO dto = new BidDTO(1L, 3L, "Proposal", 3000.0, 10L, null);

        Freelancer freelancer = new Freelancer();
        freelancer.setId(1L);

        Project project = new Project();
        project.setId(3L);
        project.setStatus(Project.ProjectStatus.OPEN);

        when(freelancerRepo.findById(1L)).thenReturn(Optional.of(freelancer));
        when(projectRepo.findById(3L)).thenReturn(Optional.of(project));
        when(bidRepo.existsByFreelancerIdAndProjectId(1L, 3L)).thenReturn(true);

        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> bidService.placeBid(dto));

        assertEquals("Freelancer has already placed a bid on this project", ex.getMessage());
    }

    @Test
    void placeBid_success_savesPendingBid() {
        BidDTO dto = new BidDTO(1L, 3L, "Detailed proposal", 7000.0, 25L, null);

        Freelancer freelancer = new Freelancer();
        freelancer.setId(1L);

        Project project = new Project();
        project.setId(3L);
        project.setStatus(Project.ProjectStatus.OPEN);

        when(freelancerRepo.findById(1L)).thenReturn(Optional.of(freelancer));
        when(projectRepo.findById(3L)).thenReturn(Optional.of(project));
        when(bidRepo.existsByFreelancerIdAndProjectId(1L, 3L)).thenReturn(false);
        when(bidRepo.save(any(Bids.class))).thenAnswer(invocation -> {
            Bids bid = invocation.getArgument(0);
            bid.setId(99L);
            return bid;
        });

        Bids saved = bidService.placeBid(dto);

        assertEquals(99L, saved.getId());
        assertEquals("Detailed proposal", saved.getProposal());
        assertEquals(Bids.bidStatus.Pending, saved.getStatus());
        assertEquals(1L, saved.getFreelancer().getId());
        assertEquals(3L, saved.getProject().getId());
        assertNotNull(saved.getCreatedAt());
    }

    @Test
    void getBidsByFreelancerId_mapsBidsToResponseDtos() {
        Freelancer freelancer = new Freelancer();
        freelancer.setId(4L);

        Project project = new Project();
        project.setId(5L);

        Bids bid = new Bids();
        bid.setId(6L);
        bid.setFreelancer(freelancer);
        bid.setProject(project);
        bid.setProposal("Sample");
        bid.setBidAmount(1200.0);
        bid.setDurationDays(12L);
        bid.setStatus(Bids.bidStatus.Accepted);
        bid.setCreatedAt(LocalDateTime.of(2026, 2, 1, 10, 0));

        when(bidRepo.findByFreelancer_Id(4L)).thenReturn(List.of(bid));

        List<BidResponseDTO> result = bidService.getBidsByFreelancerId(4L);

        assertEquals(1, result.size());
        assertEquals(6L, result.getFirst().getBidId());
        assertEquals(4L, result.getFirst().getFreelancerId());
        assertEquals(5L, result.getFirst().getProject().getId());
        assertEquals("Accepted", result.getFirst().getStatus());
    }

    @Test
    void deleteBid_whenMissing_throwsRuntimeException() {
        when(bidRepo.findById(10L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> bidService.deleteBid(10L, 3L));

        assertEquals("Bid not found", ex.getMessage());
    }

    @Test
    void deleteBid_whenNotOwnedByFreelancer_throwsRuntimeException() {
        Freelancer owner = new Freelancer();
        owner.setId(4L);

        Bids bid = new Bids();
        bid.setId(10L);
        bid.setFreelancer(owner);
        bid.setStatus(Bids.bidStatus.Pending);

        when(bidRepo.findById(10L)).thenReturn(Optional.of(bid));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> bidService.deleteBid(10L, 3L));

        assertEquals("You can only delete your own bids.", ex.getMessage());
    }

    @Test
    void deleteBid_whenNotPending_throwsRuntimeException() {
        Freelancer owner = new Freelancer();
        owner.setId(3L);

        Bids bid = new Bids();
        bid.setId(10L);
        bid.setFreelancer(owner);
        bid.setStatus(Bids.bidStatus.Rejected);

        when(bidRepo.findById(10L)).thenReturn(Optional.of(bid));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> bidService.deleteBid(10L, 3L));

        assertEquals("Only pending bids can be deleted.", ex.getMessage());
    }

    @Test
    void deleteBid_whenOwnedAndPending_deletesBid() {
        Freelancer owner = new Freelancer();
        owner.setId(3L);

        Bids bid = new Bids();
        bid.setId(10L);
        bid.setFreelancer(owner);
        bid.setStatus(Bids.bidStatus.Pending);

        when(bidRepo.findById(10L)).thenReturn(Optional.of(bid));

        bidService.deleteBid(10L, 3L);

        verify(bidRepo).deleteById(10L);
    }
}


