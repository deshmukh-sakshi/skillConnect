package com.skillconnect.backend.Chat.Scheduler;

import com.skillconnect.backend.Chat.Entity.ChatRoom;
import com.skillconnect.backend.Chat.Repository.ChatRoomRepository;
import com.skillconnect.backend.Chat.Service.ChatService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatSchedulerTest {

    @Mock
    private ChatRoomRepository chatRoomRepository;

    @Mock
    private ChatService chatService;

    @InjectMocks
    private ChatScheduler chatScheduler;

    @Test
    void archiveCompletedContractChats_archivesEligibleChatRooms() {
        ChatRoom room = new ChatRoom();
        room.setId(10L);
        room.setStatus(ChatRoom.ChatStatus.ACTIVE);
        room.setChatType(ChatRoom.ChatType.CONTRACT);

        when(chatRoomRepository.findContractChatsToArchive(any(LocalDateTime.class))).thenReturn(List.of(room));
        when(chatRoomRepository.save(room)).thenReturn(room);

        chatScheduler.archiveCompletedContractChats();

        assertEquals(ChatRoom.ChatStatus.ARCHIVED, room.getStatus());
        verify(chatService).sendSystemNotification(10L,
                "This chat room is now being archived as the contract has been completed or cancelled.");
        verify(chatRoomRepository).save(room);
    }

    @Test
    void markContractChatForArchiving_whenChatExists_sendsNotification() {
        ChatRoom room = new ChatRoom();
        room.setId(11L);
        room.setChatType(ChatRoom.ChatType.CONTRACT);

        when(chatRoomRepository.findByChatTypeAndReferenceId(ChatRoom.ChatType.CONTRACT, 90L))
                .thenReturn(Optional.of(room));

        chatScheduler.markContractChatForArchiving(90L);

        verify(chatService).sendSystemNotification(11L,
                "This contract has been completed or cancelled. The chat room will be archived in 7 days.");
    }

    @Test
    void markContractChatForArchiving_whenChatMissing_skipsNotification() {
        when(chatRoomRepository.findByChatTypeAndReferenceId(ChatRoom.ChatType.CONTRACT, 91L))
                .thenReturn(Optional.empty());

        chatScheduler.markContractChatForArchiving(91L);

        verify(chatService, never()).sendSystemNotification(anyLong(), anyString());
    }
}
