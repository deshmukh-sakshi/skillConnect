package com.skillconnect.backend.Service.contract;

import com.skillconnect.backend.Chat.Scheduler.ChatScheduler;
import com.skillconnect.backend.Chat.Service.ChatService;
import com.skillconnect.backend.DTO.ApiResponse;
import com.skillconnect.backend.DTO.ContractResponse;
import com.skillconnect.backend.Entity.Bids;
import com.skillconnect.backend.Entity.Client;
import com.skillconnect.backend.Entity.Contract;
import com.skillconnect.backend.Entity.Freelancer;
import com.skillconnect.backend.Entity.Project;
import com.skillconnect.backend.Repository.ContractRepository;
import com.skillconnect.backend.Wallet.Service.WalletService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContractServiceImplTest {

    @Mock
    private ContractRepository contractRepository;

    @Mock
    private WalletService walletService;

    @Mock
    private ChatService chatService;

    @Mock
    private ChatScheduler chatScheduler;

    @InjectMocks
    private ContractServiceImpl contractService;

    @Test
    void createContract_withValidBid_savesAndTriggersChatConversion() {
        Project project = new Project();
        project.setId(101L);
        project.setTitle("Marketplace build");

        Bids bid = new Bids();
        bid.setId(102L);
        bid.setProject(project);
        bid.setBidAmount(5000.0);

        when(contractRepository.save(any(Contract.class))).thenAnswer(invocation -> {
            Contract contract = invocation.getArgument(0);
            contract.setContractId(103L);
            contract.setContractStatus(Contract.ContractStatus.IN_PROGRESS);
            return contract;
        });

        Long contractId = contractService.createContract(bid);

        assertEquals(103L, contractId);
        verify(contractRepository).save(any(Contract.class));
        verify(chatService).convertToContractChat(102L, 103L);
    }

    @Test
    void createContract_withMissingProjectOnBid_throwsNullPointerException() {
        Bids bid = new Bids();
        bid.setId(12L);
        bid.setProject(null);

        assertThrows(NullPointerException.class, () -> contractService.createContract(bid));

        verify(contractRepository, never()).save(any(Contract.class));
    }

    @Test
    void createContract_whenConversionFails_usesLegacyTransition() {
        Project project = new Project();
        project.setId(101L);
        project.setTitle("Platform refresh");

        Bids bid = new Bids();
        bid.setId(22L);
        bid.setProject(project);
        bid.setBidAmount(2500.0);

        when(contractRepository.save(any(Contract.class))).thenAnswer(invocation -> {
            Contract contract = invocation.getArgument(0);
            contract.setContractId(203L);
            return contract;
        });
        doThrow(new RuntimeException("convert failed"))
                .when(chatService).convertToContractChat(22L, 203L);

        Long contractId = contractService.createContract(bid);

        assertEquals(203L, contractId);
        verify(chatService).transitionBidChatToContract(22L, 203L);
        verify(chatService, never()).createContractChat(anyLong());
    }

    @Test
    void createContract_whenConversionAndLegacyFail_createsNewContractChat() {
        Project project = new Project();
        project.setId(104L);
        project.setTitle("Client portal");

        Bids bid = new Bids();
        bid.setId(24L);
        bid.setProject(project);
        bid.setBidAmount(4100.0);

        when(contractRepository.save(any(Contract.class))).thenAnswer(invocation -> {
            Contract contract = invocation.getArgument(0);
            contract.setContractId(205L);
            return contract;
        });
        doThrow(new RuntimeException("convert failed"))
                .when(chatService).convertToContractChat(24L, 205L);
        doThrow(new RuntimeException("legacy failed"))
                .when(chatService).transitionBidChatToContract(24L, 205L);

        Long contractId = contractService.createContract(bid);

        assertEquals(205L, contractId);
        verify(chatService).createContractChat(205L);
    }

    @Test
    void getAllContracts_returnsSuccessEnvelopeAndMappedDtos() {
        Project project = new Project();
        project.setId(100L);

        Bids bid = new Bids();
        bid.setId(200L);

        Contract contract = new Contract();
        contract.setContractId(300L);
        contract.setProject(project);
        contract.setBid(bid);
        contract.setContractStatus(Contract.ContractStatus.IN_PROGRESS);
        contract.setCreatedAt(LocalDateTime.of(2026, 3, 20, 10, 0));
        contract.setUpdatedAt(LocalDateTime.of(2026, 3, 21, 11, 0));

        when(contractRepository.findAll()).thenReturn(List.of(contract));

        ApiResponse<List<ContractResponse>> response = contractService.getAllContracts();

        assertEquals("success", response.getStatus());
        assertNotNull(response.getData());
        assertEquals(1, response.getData().size());

        ContractResponse dto = response.getData().getFirst();
        assertEquals(300L, dto.getContractId());
        assertEquals(100L, dto.getProjectId());
        assertEquals(200L, dto.getBidId());
        assertEquals("IN_PROGRESS", dto.getContractStatus());
    }

    @Test
    void getContractById_whenFound_returnsSuccessEnvelope() {
        Project project = new Project();
        project.setId(9L);

        Bids bid = new Bids();
        bid.setId(8L);

        Contract contract = new Contract();
        contract.setContractId(7L);
        contract.setProject(project);
        contract.setBid(bid);
        contract.setContractStatus(Contract.ContractStatus.COMPLETED);

        when(contractRepository.findById(7L)).thenReturn(Optional.of(contract));

        ApiResponse<ContractResponse> response = contractService.getContractById(7L);

        assertEquals("success", response.getStatus());
        assertNotNull(response.getData());
        assertEquals(7L, response.getData().getContractId());
        assertEquals("COMPLETED", response.getData().getContractStatus());
    }

    @Test
    void updateContract_whenCompleted_releasesPaymentAndMarksChatForArchiving() {
        Client client = new Client();
        client.setId(9L);

        Freelancer freelancer = new Freelancer();
        freelancer.setId(8L);

        Project project = new Project();
        project.setId(7L);
        project.setTitle("Billing module");
        project.setClient(client);

        Bids bid = new Bids();
        bid.setId(6L);
        bid.setBidAmount(1750.0);
        bid.setFreelancer(freelancer);
        bid.setProject(project);

        Contract contract = new Contract();
        contract.setContractId(5L);
        contract.setProject(project);
        contract.setBid(bid);
        contract.setContractStatus(Contract.ContractStatus.IN_PROGRESS);

        when(contractRepository.findById(5L)).thenReturn(Optional.of(contract));
        when(contractRepository.save(any(Contract.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ApiResponse<ContractResponse> response = contractService.updateContract(5L, "{\"contractStatus\":\"COMPLETED\"}");

        assertEquals("success", response.getStatus());
        assertEquals("COMPLETED", response.getData().getContractStatus());
        verify(walletService).releasePayment(9L, 8L, 7L, 1750.0);
        verify(chatService).sendSystemNotification(5L, "Contract status changed from IN_PROGRESS to COMPLETED");
        verify(chatScheduler).markContractChatForArchiving(5L);
    }

    @Test
    void updateContract_withInvalidPayload_throwsRuntimeException() {
        Project project = new Project();
        project.setId(1L);
        project.setTitle("Internal tool");

        Bids bid = new Bids();
        bid.setId(2L);
        bid.setBidAmount(999.0);
        bid.setProject(project);

        Contract contract = new Contract();
        contract.setContractId(3L);
        contract.setProject(project);
        contract.setBid(bid);
        contract.setContractStatus(Contract.ContractStatus.IN_PROGRESS);

        when(contractRepository.findById(3L)).thenReturn(Optional.of(contract));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> contractService.updateContract(3L, "{\"contractStatus\":\"INVALID\"}"));

        assertEquals("Invalid contract update payload", ex.getMessage());
        verify(contractRepository, never()).save(any(Contract.class));
    }

    @Test
    void deleteContract_sendsNotificationAndDeletesContract() {
        Project project = new Project();
        project.setId(3L);

        Bids bid = new Bids();
        bid.setId(4L);
        bid.setProject(project);

        Contract contract = new Contract();
        contract.setContractId(10L);
        contract.setProject(project);
        contract.setBid(bid);

        when(contractRepository.findById(10L)).thenReturn(Optional.of(contract));

        ApiResponse<String> response = contractService.deleteContract(10L);

        assertEquals("success", response.getStatus());
        assertEquals("Contract deleted successfully", response.getData());
        verify(chatService).sendSystemNotification(10L, "Contract is being deleted. This chat will be closed.");
        verify(contractRepository).delete(contract);
    }

    @Test
    void getContractById_whenMissing_throwsRuntimeException() {
        when(contractRepository.findById(55L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> contractService.getContractById(55L));

        assertEquals("Contract not found", ex.getMessage());
    }
}


