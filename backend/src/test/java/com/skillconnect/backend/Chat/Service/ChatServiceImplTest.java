package com.skillconnect.backend.Chat.Service;

import com.skillconnect.backend.Chat.DTO.BidDetailsResponse;
import com.skillconnect.backend.Chat.Entity.ChatMessage;
import com.skillconnect.backend.Chat.Entity.ChatRoom;
import com.skillconnect.backend.Chat.Repository.ChatMessageRepository;
import com.skillconnect.backend.Chat.Repository.ChatRoomRepository;
import com.skillconnect.backend.Entity.*;
import com.skillconnect.backend.Repository.BidRepository;
import com.skillconnect.backend.Repository.ClientRepository;
import com.skillconnect.backend.Repository.ContractRepository;
import com.skillconnect.backend.Repository.FreelancerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatServiceImplTest {

    @Mock
    private ChatRoomRepository chatRoomRepository;

    @Mock
    private ChatMessageRepository chatMessageRepository;

    @Mock
    private BidRepository bidRepository;

    @Mock
    private ContractRepository contractRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private FreelancerRepository freelancerRepository;

    @Spy
    @InjectMocks
    private ChatServiceImpl chatService;

    @Test
    void createBidNegotiationChat_whenExisting_returnsExistingRoom() {
        ChatRoom existing = new ChatRoom();
        existing.setId(12L);

        when(chatRoomRepository.findByChatTypeAndReferenceId(ChatRoom.ChatType.BID_NEGOTIATION, 10L))
                .thenReturn(Optional.of(existing));

        ChatRoom result = chatService.createBidNegotiationChat(10L);

        assertSame(existing, result);
        verify(bidRepository, never()).findById(anyLong());
        verify(chatRoomRepository, never()).save(any(ChatRoom.class));
    }

    @Test
    void createBidNegotiationChat_whenMissing_createsRoomAndSendsWelcomeNotification() {
        Bids bid = buildBid(10L, 100L, 200L);
        doNothing().when(chatService).sendSystemNotification(anyLong(), anyString());

        when(chatRoomRepository.findByChatTypeAndReferenceId(ChatRoom.ChatType.BID_NEGOTIATION, 10L))
                .thenReturn(Optional.empty());
        when(bidRepository.findById(10L)).thenReturn(Optional.of(bid));
        when(chatRoomRepository.save(any(ChatRoom.class))).thenAnswer(invocation -> {
            ChatRoom room = invocation.getArgument(0);
            room.setId(44L);
            return room;
        });

        ChatRoom created = chatService.createBidNegotiationChat(10L);

        assertEquals(44L, created.getId());
        assertEquals(ChatRoom.ChatType.BID_NEGOTIATION, created.getChatType());
        assertEquals(10L, created.getReferenceId());
        assertEquals(10L, created.getOriginalBidId());
        assertEquals(ChatRoom.ChatStatus.ACTIVE, created.getStatus());
        assertEquals(100L, created.getClient().getId());
        assertEquals(200L, created.getFreelancer().getId());
        verify(chatService).sendSystemNotification(eq(44L), contains("Chat created for bid negotiation"));
    }

    @Test
    void convertToContractChat_whenContractChatExists_returnsExistingContractRoom() {
        ChatRoom existingContractRoom = new ChatRoom();
        existingContractRoom.setId(200L);

        when(chatRoomRepository.findByChatTypeAndReferenceId(ChatRoom.ChatType.CONTRACT, 88L))
                .thenReturn(Optional.of(existingContractRoom));

        ChatRoom result = chatService.convertToContractChat(44L, 88L);

        assertSame(existingContractRoom, result);
        verify(chatRoomRepository, never()).save(any(ChatRoom.class));
    }

    @Test
    void convertToContractChat_whenBidRoomExists_updatesRoomAndSendsTransitionNotification() {
        ChatRoom bidRoom = new ChatRoom();
        bidRoom.setId(50L);
        bidRoom.setChatType(ChatRoom.ChatType.BID_NEGOTIATION);
        bidRoom.setReferenceId(44L);
        bidRoom.setStatus(ChatRoom.ChatStatus.ACTIVE);
        doNothing().when(chatService).sendSystemNotification(anyLong(), anyString());

        when(chatRoomRepository.findByChatTypeAndReferenceId(ChatRoom.ChatType.CONTRACT, 88L))
                .thenReturn(Optional.empty());
        when(chatRoomRepository.findByChatTypeAndReferenceId(ChatRoom.ChatType.BID_NEGOTIATION, 44L))
                .thenReturn(Optional.of(bidRoom));
        when(chatRoomRepository.save(bidRoom)).thenReturn(bidRoom);

        ChatRoom updated = chatService.convertToContractChat(44L, 88L);

        assertEquals(ChatRoom.ChatType.CONTRACT, updated.getChatType());
        assertEquals(88L, updated.getReferenceId());
        assertEquals(44L, updated.getOriginalBidId());
        assertEquals(ChatRoom.ChatStatus.ACTIVE, updated.getStatus());
        verify(chatService).sendSystemNotification(eq(50L), contains("converted to a contract chat"));
    }

    @Test
    void closeBidChat_marksRoomClosedAndSendsClosureNotification() {
        ChatRoom bidRoom = new ChatRoom();
        bidRoom.setId(70L);
        bidRoom.setChatType(ChatRoom.ChatType.BID_NEGOTIATION);
        bidRoom.setReferenceId(40L);
        bidRoom.setStatus(ChatRoom.ChatStatus.ACTIVE);
        doNothing().when(chatService).sendSystemNotification(anyLong(), anyString());

        when(chatRoomRepository.findByChatTypeAndReferenceId(ChatRoom.ChatType.BID_NEGOTIATION, 40L))
                .thenReturn(Optional.of(bidRoom));
        when(chatRoomRepository.save(bidRoom)).thenReturn(bidRoom);

        chatService.closeBidChat(40L);

        assertEquals(ChatRoom.ChatStatus.CLOSED, bidRoom.getStatus());
        verify(chatService).sendSystemNotification(eq(70L), contains("chat is now closed"));
    }

    @Test
    void getBidDetailsForChat_whenClientOwnsPendingBid_allowsAcceptAndReject() {
        Client client = new Client();
        client.setId(100L);
        client.setName("Client One");

        Freelancer freelancer = new Freelancer();
        freelancer.setId(200L);
        freelancer.setName("Freelancer One");

        Project project = new Project();
        project.setId(300L);
        project.setTitle("Landing page");
        project.setClient(client);

        Bids bid = new Bids();
        bid.setId(44L);
        bid.setProject(project);
        bid.setFreelancer(freelancer);
        bid.setProposal("I can deliver this in 7 days");
        bid.setBidAmount(1500.0);
        bid.setDurationDays(7L);
        bid.setTeamSize(2);
        bid.setStatus(Bids.bidStatus.Pending);

        ChatRoom bidRoom = new ChatRoom();
        bidRoom.setId(10L);
        bidRoom.setChatType(ChatRoom.ChatType.BID_NEGOTIATION);
        bidRoom.setReferenceId(44L);
        bidRoom.setClient(client);
        bidRoom.setFreelancer(freelancer);

        when(chatRoomRepository.findById(10L)).thenReturn(Optional.of(bidRoom));
        when(bidRepository.findById(44L)).thenReturn(Optional.of(bid));

        BidDetailsResponse response = chatService.getBidDetailsForChat(10L, 100L, "CLIENT");

        assertEquals(44L, response.getBidId());
        assertEquals(300L, response.getProjectId());
        assertTrue(response.isCanAccept());
        assertTrue(response.isCanReject());
    }

    @Test
    void sendBidSystemNotification_whenRejected_closesBidChatAfterSendingNotification() {
        ChatRoom bidRoom = new ChatRoom();
        bidRoom.setId(81L);
        bidRoom.setChatType(ChatRoom.ChatType.BID_NEGOTIATION);
        bidRoom.setReferenceId(55L);
        bidRoom.setStatus(ChatRoom.ChatStatus.ACTIVE);
        doNothing().when(chatService).sendSystemNotification(anyLong(), anyString());
        doNothing().when(chatService).sendSystemNotification(anyLong(), anyString(), any(ChatMessage.MessageType.class));

        when(chatRoomRepository.findByChatTypeAndReferenceId(ChatRoom.ChatType.BID_NEGOTIATION, 55L))
                .thenReturn(Optional.of(bidRoom));
        when(chatRoomRepository.save(any(ChatRoom.class))).thenAnswer(invocation -> invocation.getArgument(0));

        chatService.sendBidSystemNotification(55L, "Bid has been rejected.");

        verify(chatService).sendSystemNotification(81L, "Bid has been rejected.", ChatMessage.MessageType.BID_ACTION);
        verify(chatService).closeBidChat(55L);
        assertEquals(ChatRoom.ChatStatus.CLOSED, bidRoom.getStatus());
    }

    private Bids buildBid(Long bidId, Long clientId, Long freelancerId) {
        Client client = new Client();
        client.setId(clientId);
        client.setName("Client");

        Freelancer freelancer = new Freelancer();
        freelancer.setId(freelancerId);
        freelancer.setName("Freelancer");

        Project project = new Project();
        project.setId(99L);
        project.setTitle("Project X");
        project.setClient(client);

        Bids bid = new Bids();
        bid.setId(bidId);
        bid.setProject(project);
        bid.setFreelancer(freelancer);
        bid.setStatus(Bids.bidStatus.Pending);
        return bid;
    }

    @SuppressWarnings("unused")
    private Contract buildContract(Long contractId, Bids bid) {
        Contract contract = new Contract();
        contract.setContractId(contractId);
        contract.setBid(bid);
        contract.setProject(bid.getProject());
        return contract;
    }
}
