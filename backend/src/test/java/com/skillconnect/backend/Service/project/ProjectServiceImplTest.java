package com.skillconnect.backend.Service.project;

import com.skillconnect.backend.Chat.Service.ChatService;
import com.skillconnect.backend.DTO.BidResponseDTO;
import com.skillconnect.backend.DTO.ClientDTO;
import com.skillconnect.backend.DTO.ProjectDTO;
import com.skillconnect.backend.Entity.Bids;
import com.skillconnect.backend.Entity.Client;
import com.skillconnect.backend.Entity.Freelancer;
import com.skillconnect.backend.Entity.Project;
import com.skillconnect.backend.Repository.BidRepository;
import com.skillconnect.backend.Repository.ClientRepository;
import com.skillconnect.backend.Repository.ProjectRepository;
import com.skillconnect.backend.Service.contract.ContractService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceImplTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private BidRepository bidRepo;

    @Mock
    private ContractService contractService;

    @Mock
    private com.skillconnect.backend.Wallet.Service.WalletService walletService;

    @Mock
    private ChatService chatService;

    @InjectMocks
    private ProjectServiceImpl projectService;

    @Test
    void createProject_success_mapsAndReturnsDto() {
        ProjectDTO input = new ProjectDTO(
                null,
                "Portfolio website",
                "Build a personal portfolio",
                "Web Development",
                LocalDateTime.of(2026, 4, 1, 12, 0),
                25000L,
                null,
                null,
                7L
        );

        Client client = new Client();
        client.setId(7L);

        when(clientRepository.findById(7L)).thenReturn(Optional.of(client));
        when(projectRepository.save(any(Project.class))).thenAnswer(invocation -> {
            Project p = invocation.getArgument(0);
            p.setId(99L);
            p.setStatus(Project.ProjectStatus.OPEN);
            return p;
        });

        ProjectDTO result = projectService.createProject(input);

        assertEquals(99L, result.getId());
        assertEquals("Portfolio website", result.getTitle());
        assertEquals("Build a personal portfolio", result.getDescription());
        assertEquals("Web Development", result.getCategory());
        assertEquals(25000L, result.getBudget());
        assertEquals(Project.ProjectStatus.OPEN, result.getStatus());
        assertEquals(7L, result.getClientId());

        ArgumentCaptor<Project> captor = ArgumentCaptor.forClass(Project.class);
        verify(projectRepository).save(captor.capture());
        assertSame(client, captor.getValue().getClient());
    }

    @Test
    void createProject_clientNotFound_throwsRuntimeException() {
        ProjectDTO input = new ProjectDTO(
                null,
                "API Project",
                "No client exists",
                "Backend",
                LocalDateTime.of(2026, 5, 1, 9, 30),
                15000L,
                null,
                null,
                123L
        );

        when(clientRepository.findById(123L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> projectService.createProject(input));

        assertEquals("Client not found", ex.getMessage());
        verify(projectRepository, never()).save(any(Project.class));
    }

    @Test
    void getProjectById_notFound_throwsRuntimeException() {
        when(projectRepository.findById(44L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> projectService.getProjectById(44L));

        assertEquals("Project not found", ex.getMessage());
    }

    @Test
    void getProjectById_found_returnsMappedDto() {
        Project project = new Project();
        project.setId(11L);
        project.setTitle("Mobile app");
        project.setDescription("Build iOS app");
        project.setCategory("Mobile");
        project.setDeadline(LocalDateTime.of(2026, 6, 1, 10, 0));
        project.setBudget(90000L);
        project.setStatus(Project.ProjectStatus.OPEN);

        Client client = new Client();
        client.setId(2L);
        project.setClient(client);

        when(projectRepository.findById(11L)).thenReturn(Optional.of(project));

        ProjectDTO result = projectService.getProjectById(11L);

        assertEquals(11L, result.getId());
        assertEquals("Mobile app", result.getTitle());
        assertEquals("Mobile", result.getCategory());
        assertEquals(2L, result.getClientId());
    }

    @Test
    void getAllProjects_returnsMappedDtos() {
        Client client = new Client();
        client.setId(3L);

        Project project = new Project();
        project.setId(21L);
        project.setTitle("Ecommerce");
        project.setDescription("Build ecommerce website");
        project.setCategory("Web");
        project.setDeadline(LocalDateTime.of(2026, 7, 1, 9, 0));
        project.setBudget(120000L);
        project.setStatus(Project.ProjectStatus.OPEN);
        project.setClient(client);

        when(projectRepository.findAll()).thenReturn(List.of(project));

        List<ProjectDTO> result = projectService.getAllProjects(null);

        assertEquals(1, result.size());
        assertEquals(21L, result.get(0).getId());
        assertEquals("Web", result.get(0).getCategory());
        assertEquals(3L, result.get(0).getClientId());
    }

    @Test
    void getAllProjects_withQuery_usesSearchRepository() {
        Client client = new Client();
        client.setId(13L);

        Project project = new Project();
        project.setId(31L);
        project.setTitle("Design system");
        project.setDescription("Build shared components");
        project.setCategory("Design");
        project.setDeadline(LocalDateTime.of(2026, 10, 1, 10, 0));
        project.setBudget(45000L);
        project.setStatus(Project.ProjectStatus.OPEN);
        project.setClient(client);

        when(projectRepository.findByTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase("design", "design"))
                .thenReturn(List.of(project));

        List<ProjectDTO> result = projectService.getAllProjects("  design ");

        assertEquals(1, result.size());
        assertEquals(31L, result.getFirst().getId());
        verify(projectRepository).findByTitleContainingIgnoreCaseOrCategoryContainingIgnoreCase("design", "design");
        verify(projectRepository, never()).findAll();
    }

    @Test
    void updateProject_notFound_throwsRuntimeException() {
        when(projectRepository.findById(10L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> projectService.updateProject(10L, new ProjectDTO()));

        assertEquals("Project not found", ex.getMessage());
    }

    @Test
    void updateProject_withClientId_updatesClientAndFields() {
        Client oldClient = new Client();
        oldClient.setId(1L);
        Client newClient = new Client();
        newClient.setId(8L);

        Project existing = new Project();
        existing.setId(13L);
        existing.setClient(oldClient);

        ProjectDTO input = new ProjectDTO(
                null,
                "Updated title",
                "Updated description",
                "Design",
                LocalDateTime.of(2026, 8, 1, 11, 0),
                30000L,
                null,
                null,
                8L
        );

        when(projectRepository.findById(13L)).thenReturn(Optional.of(existing));
        when(clientRepository.findById(8L)).thenReturn(Optional.of(newClient));
        when(projectRepository.save(existing)).thenReturn(existing);

        ProjectDTO result = projectService.updateProject(13L, input);

        assertEquals("Updated title", result.getTitle());
        assertEquals("Design", result.getCategory());
        assertEquals(8L, result.getClientId());
        verify(projectRepository).save(existing);
    }

    @Test
    void updateProject_withMissingClient_throwsRuntimeException() {
        Project existing = new Project();
        existing.setId(14L);

        ProjectDTO input = new ProjectDTO(
                null,
                "Title",
                "Description",
                "Data",
                LocalDateTime.of(2026, 9, 1, 10, 0),
                45000L,
                null,
                null,
                99L
        );

        when(projectRepository.findById(14L)).thenReturn(Optional.of(existing));
        when(clientRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> projectService.updateProject(14L, input));

        assertEquals("Client not found", ex.getMessage());
        verify(projectRepository, never()).save(any(Project.class));
    }

    @Test
    void getBidsByProjectId_returnsMappedDtos() {
        Project project = new Project();
        project.setId(16L);

        Freelancer freelancer = new Freelancer();
        freelancer.setId(22L);

        Bids bid = new Bids();
        bid.setId(33L);
        bid.setProject(project);
        bid.setFreelancer(freelancer);
        bid.setProposal("I can deliver in 2 weeks");
        bid.setBidAmount(5000);
        bid.setDurationDays(14L);
        bid.setStatus(Bids.bidStatus.Pending);
        bid.setCreatedAt(LocalDateTime.of(2026, 3, 1, 9, 0));

        when(bidRepo.findByProject_Id(16L)).thenReturn(List.of(bid));

        List<BidResponseDTO> result = projectService.getBidsByProjectId(16L);

        assertEquals(1, result.size());
        assertEquals(33L, result.get(0).getBidId());
        assertEquals(22L, result.get(0).getFreelancerId());
        assertEquals("Pending", result.get(0).getStatus());
    }

    @Test
    void acceptBid_bidNotFound_throwsRuntimeException() {
        when(bidRepo.findById(20L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> projectService.acceptBid(1L, 20L));

        assertEquals("Bid not found", ex.getMessage());
    }

    @Test
    void acceptBid_projectMismatch_throwsRuntimeException() {
        Project project = new Project();
        project.setId(2L);

        Bids acceptedBid = new Bids();
        acceptedBid.setId(5L);
        acceptedBid.setProject(project);

        when(bidRepo.findById(5L)).thenReturn(Optional.of(acceptedBid));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> projectService.acceptBid(1L, 5L));

        assertEquals("Bid does not belong to the specified project", ex.getMessage());
    }

    @Test
    void acceptBid_success_closesProjectAndUpdatesAllBidStatuses() {
        Client client = new Client();
        client.setId(10L);

        Project project = new Project();
        project.setId(4L);
        project.setStatus(Project.ProjectStatus.OPEN);
        project.setClient(client);

        Bids target = new Bids();
        target.setId(40L);
        target.setProject(project);
        target.setStatus(Bids.bidStatus.Pending);
        target.setBidAmount(500.0);

        Bids other = new Bids();
        other.setId(41L);
        other.setProject(project);
        other.setStatus(Bids.bidStatus.Pending);

        when(bidRepo.findById(40L)).thenReturn(Optional.of(target));
        when(bidRepo.findByProject_Id(4L)).thenReturn(List.of(target, other));
        when(contractService.createContract(target)).thenReturn(501L);

        projectService.acceptBid(4L, 40L);

        assertEquals(Project.ProjectStatus.CLOSED, project.getStatus());
        assertEquals(Bids.bidStatus.Accepted, target.getStatus());
        assertEquals(Bids.bidStatus.Rejected, other.getStatus());
        verify(projectRepository).save(project);
        verify(bidRepo).saveAll(List.of(target, other));
        verify(walletService).freezeAmount(10L, 4L, 500.0);
        verify(contractService).createContract(target);
        verify(chatService).closeBidChat(41L);
        verify(chatService).convertToContractChat(40L, 501L);
    }

    @Test
    void rejectBid_bidNotFound_throwsRuntimeException() {
        when(bidRepo.findById(88L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> projectService.rejectBid(3L, 88L));

        assertEquals("Bid not found", ex.getMessage());
    }

    @Test
    void rejectBid_projectMismatch_throwsRuntimeException() {
        Project project = new Project();
        project.setId(9L);

        Bids bid = new Bids();
        bid.setId(89L);
        bid.setProject(project);
        bid.setStatus(Bids.bidStatus.Pending);

        when(bidRepo.findById(89L)).thenReturn(Optional.of(bid));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> projectService.rejectBid(10L, 89L));

        assertEquals("Bid does not belong to the specified project", ex.getMessage());
    }

    @Test
    void rejectBid_nonPending_throwsIllegalStateException() {
        Project project = new Project();
        project.setId(15L);

        Bids bid = new Bids();
        bid.setId(90L);
        bid.setProject(project);
        bid.setStatus(Bids.bidStatus.Accepted);

        when(bidRepo.findById(90L)).thenReturn(Optional.of(bid));

        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> projectService.rejectBid(15L, 90L));

        assertEquals("Only pending bids can be rejected", ex.getMessage());
    }

    @Test
    void rejectBid_pendingBid_updatesStatusToRejected() {
        Project project = new Project();
        project.setId(25L);

        Bids bid = new Bids();
        bid.setId(91L);
        bid.setProject(project);
        bid.setStatus(Bids.bidStatus.Pending);

        when(bidRepo.findById(91L)).thenReturn(Optional.of(bid));

        projectService.rejectBid(25L, 91L);

        assertEquals(Bids.bidStatus.Rejected, bid.getStatus());
        verify(bidRepo).save(bid);
        verify(chatService).sendBidSystemNotification(91L, "Bid has been rejected.");
        verify(chatService).closeBidChat(91L);
    }

    @Test
    void deleteProjectById_whenExists_deletesAndReturnsTrue() {
        when(projectRepository.existsById(5L)).thenReturn(true);

        boolean deleted = projectService.deleteProjectById(5L);

        assertTrue(deleted);
        verify(projectRepository).deleteById(5L);
    }

    @Test
    void deleteProjectById_whenMissing_returnsFalse() {
        when(projectRepository.existsById(6L)).thenReturn(false);

        boolean deleted = projectService.deleteProjectById(6L);

        assertFalse(deleted);
        verify(projectRepository, never()).deleteById(anyLong());
    }
}

