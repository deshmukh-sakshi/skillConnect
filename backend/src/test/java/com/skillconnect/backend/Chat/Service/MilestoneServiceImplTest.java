package com.skillconnect.backend.Chat.Service;

import com.skillconnect.backend.Chat.DTO.MilestoneRequest;
import com.skillconnect.backend.Chat.DTO.MilestoneResponse;
import com.skillconnect.backend.Chat.Entity.ChatMessage;
import com.skillconnect.backend.Chat.Entity.ChatRoom;
import com.skillconnect.backend.Chat.Entity.Milestone;
import com.skillconnect.backend.Chat.Enum.MilestoneStatus;
import com.skillconnect.backend.Chat.Repository.ChatRoomRepository;
import com.skillconnect.backend.Chat.Repository.MilestoneRepository;
import com.skillconnect.backend.Entity.Contract;
import com.skillconnect.backend.Repository.ContractRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MilestoneServiceImplTest {

    @Mock
    private MilestoneRepository milestoneRepository;

    @Mock
    private ContractRepository contractRepository;

    @Mock
    private ChatRoomRepository chatRoomRepository;

    @Mock
    private ChatService chatService;

    @InjectMocks
    private MilestoneServiceImpl milestoneService;

    @Test
    void createMilestone_success_savesPendingMilestoneAndSendsNotification() {
        Contract contract = buildContract(40L);
        MilestoneRequest request = new MilestoneRequest("Design API", "Define endpoints", LocalDateTime.now().plusDays(5));

        when(contractRepository.findById(40L)).thenReturn(Optional.of(contract));
        when(milestoneRepository.save(any(Milestone.class))).thenAnswer(invocation -> {
            Milestone milestone = invocation.getArgument(0);
            milestone.setId(11L);
            return milestone;
        });

        ChatRoom contractChat = new ChatRoom();
        contractChat.setId(90L);
        when(chatRoomRepository.findByChatTypeAndReferenceId(ChatRoom.ChatType.CONTRACT, 40L))
                .thenReturn(Optional.of(contractChat));

        MilestoneResponse response = milestoneService.createMilestone(40L, request);

        assertEquals(11L, response.getId());
        assertEquals(40L, response.getContractId());
        assertEquals(MilestoneStatus.PENDING, response.getStatus());
        verify(chatService).sendSystemNotification(eq(90L), contains("New Milestone Created"),
                eq(ChatMessage.MessageType.MILESTONE_UPDATE));
    }

    @Test
    void createMilestone_whenContractMissing_throwsRuntimeException() {
        MilestoneRequest request = new MilestoneRequest("UI", "Missing contract", LocalDateTime.now().plusDays(2));
        when(contractRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> milestoneService.createMilestone(99L, request));

        assertEquals("Contract not found with ID: 99", ex.getMessage());
        verify(milestoneRepository, never()).save(any(Milestone.class));
    }

    @Test
    void updateMilestoneStatus_whenStatusUnchanged_skipsSaveAndNotification() {
        Contract contract = buildContract(40L);
        Milestone milestone = buildMilestone(20L, contract, MilestoneStatus.PENDING);

        when(milestoneRepository.findById(20L)).thenReturn(Optional.of(milestone));

        MilestoneResponse response = milestoneService.updateMilestoneStatus(20L, MilestoneStatus.PENDING);

        assertEquals(MilestoneStatus.PENDING, response.getStatus());
        verify(milestoneRepository, never()).save(any(Milestone.class));
        verifyNoInteractions(chatService);
    }

    @Test
    void updateMilestoneStatus_whenCompleted_sendsProgressNotification() {
        Contract contract = buildContract(40L);
        Milestone milestone = buildMilestone(21L, contract, MilestoneStatus.IN_PROGRESS);

        when(milestoneRepository.findById(21L)).thenReturn(Optional.of(milestone));
        when(milestoneRepository.save(any(Milestone.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(contractRepository.existsById(40L)).thenReturn(true);
        when(milestoneRepository.getCompletionPercentageByContractId(40L)).thenReturn(75.0);

        ChatRoom contractChat = new ChatRoom();
        contractChat.setId(91L);
        when(chatRoomRepository.findByChatTypeAndReferenceId(ChatRoom.ChatType.CONTRACT, 40L))
                .thenReturn(Optional.of(contractChat));

        MilestoneResponse response = milestoneService.updateMilestoneStatus(21L, MilestoneStatus.COMPLETED);

        assertEquals(MilestoneStatus.COMPLETED, response.getStatus());
        verify(chatService).sendSystemNotification(eq(91L), contains("Project completion: 75.0%"),
                eq(ChatMessage.MessageType.MILESTONE_UPDATE));
    }

    @Test
    void getContractCompletionPercentage_whenRepositoryReturnsNull_returnsZero() {
        when(contractRepository.existsById(40L)).thenReturn(true);
        when(milestoneRepository.getCompletionPercentageByContractId(40L)).thenReturn(null);

        Double completionPercentage = milestoneService.getContractCompletionPercentage(40L);

        assertEquals(0.0, completionPercentage);
    }

    @Test
    void getOverdueMilestones_filtersOutCompletedMilestones() {
        Contract contract = buildContract(40L);

        Milestone completed = buildMilestone(31L, contract, MilestoneStatus.COMPLETED);
        completed.setDueDate(LocalDateTime.now().minusDays(3));

        Milestone overdue = buildMilestone(32L, contract, MilestoneStatus.OVERDUE);
        overdue.setDueDate(LocalDateTime.now().minusDays(2));

        when(contractRepository.existsById(40L)).thenReturn(true);
        when(milestoneRepository.findByContractIdAndDueDateBefore(eq(40L), any(LocalDateTime.class)))
                .thenReturn(List.of(completed, overdue));

        List<MilestoneResponse> responses = milestoneService.getOverdueMilestones(40L);

        assertEquals(1, responses.size());
        assertEquals(32L, responses.getFirst().getId());
    }

    @Test
    void updateMilestoneProgressTracking_marksPendingMilestonesAsOverdueAndNotifiesChat() {
        Contract contract = buildContract(40L);
        Milestone pendingMilestone = buildMilestone(51L, contract, MilestoneStatus.PENDING);
        pendingMilestone.setDueDate(LocalDateTime.now().minusDays(1));

        when(milestoneRepository.findMilestonesNeedingStatusUpdate(any(LocalDateTime.class)))
                .thenReturn(List.of(pendingMilestone));
        when(milestoneRepository.save(any(Milestone.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ChatRoom contractChat = new ChatRoom();
        contractChat.setId(92L);
        when(chatRoomRepository.findByChatTypeAndReferenceId(ChatRoom.ChatType.CONTRACT, 40L))
                .thenReturn(Optional.of(contractChat));

        milestoneService.updateMilestoneProgressTracking();

        assertEquals(MilestoneStatus.OVERDUE, pendingMilestone.getStatus());
        verify(milestoneRepository).save(pendingMilestone);
        verify(chatService).sendSystemNotification(eq(92L), contains("Milestone Overdue"),
                eq(ChatMessage.MessageType.MILESTONE_UPDATE));
    }

    private Contract buildContract(Long contractId) {
        Contract contract = new Contract();
        contract.setContractId(contractId);
        return contract;
    }

    private Milestone buildMilestone(Long milestoneId, Contract contract, MilestoneStatus status) {
        Milestone milestone = new Milestone();
        milestone.setId(milestoneId);
        milestone.setContract(contract);
        milestone.setTitle("Milestone " + milestoneId);
        milestone.setDescription("Description");
        milestone.setStatus(status);
        milestone.setDueDate(LocalDateTime.now().plusDays(3));
        return milestone;
    }
}
