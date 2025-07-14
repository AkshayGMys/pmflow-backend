package com.example.pmflow.controller;

import com.example.pmflow.dto.ChatRequestDTO;
import com.example.pmflow.dto.ChatSummaryDTO;
import com.example.pmflow.dto.MemberProjectDTO;
import com.example.pmflow.service.ChatService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ChatControllerTest {

    @Mock
    private ChatService chatService;

    @InjectMocks
    private ChatController chatController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSendPrivateMessage() {
        ChatRequestDTO request = new ChatRequestDTO();
        request.setContent("Hello");

        ChatSummaryDTO expected = new ChatSummaryDTO(1L, "Sender Name", "Hello", LocalDateTime.now());
        when(chatService.sendPrivateMessage(1L, 2L, 3L, 4L, request)).thenReturn(expected);

        ResponseEntity<ChatSummaryDTO> response = chatController.sendPrivateMessage(1L, 2L, 3L, 4L, request);

        assertEquals(expected, response.getBody());
        verify(chatService).sendPrivateMessage(1L, 2L, 3L, 4L, request);
    }

    @Test
    void testSendGroupMessage() {
        ChatRequestDTO request = new ChatRequestDTO();
        request.setContent("Group Hello");

        ChatSummaryDTO expected = new ChatSummaryDTO(1L, "Sender Name", "Group Hello", LocalDateTime.now());
        when(chatService.sendGroupMessage(1L, 2L, request)).thenReturn(expected);

        ResponseEntity<ChatSummaryDTO> response = chatController.sendGroupMessage(1L, 2L, request);

        assertEquals(expected, response.getBody());
        verify(chatService).sendGroupMessage(1L, 2L, request);
    }

    @Test
    void testGetPrivateChatSummary() {
        ChatSummaryDTO chat1 = new ChatSummaryDTO(1L, "Sender One", "Hello", LocalDateTime.now());
        ChatSummaryDTO chat2 = new ChatSummaryDTO(2L, "Receiver", "Hi", LocalDateTime.now());
        List<ChatSummaryDTO> expected = List.of(chat1, chat2);

        when(chatService.getPrivateChatSummary(1L, 2L, 3L, 4L)).thenReturn(expected);

        ResponseEntity<List<ChatSummaryDTO>> response = chatController.getPrivateChatSummary(1L, 2L, 3L, 4L);

        assertEquals(expected, response.getBody());
        verify(chatService).getPrivateChatSummary(1L, 2L, 3L, 4L);
    }

    @Test
    void testGetGroupChatSummary() {
        ChatSummaryDTO chat = new ChatSummaryDTO(1L, "Manager", "Group message", LocalDateTime.now());
        List<ChatSummaryDTO> expected = List.of(chat);

        when(chatService.getGroupChatSummary(5L)).thenReturn(expected);

        ResponseEntity<List<ChatSummaryDTO>> response = chatController.getGroupChatSummary(5L);

        assertEquals(expected, response.getBody());
        verify(chatService).getGroupChatSummary(5L);
    }

    @Test
    void testGetAssignedProjects() {
        MemberProjectDTO project = new MemberProjectDTO(5L, "PM-Flow", "IN_PROGRESS");
        List<MemberProjectDTO> expected = List.of(project);

        when(chatService.getAssignedProjectsForCurrentUser()).thenReturn(expected);

        ResponseEntity<List<MemberProjectDTO>> response = chatController.getAssignedProjects();

        assertEquals(expected, response.getBody());
        verify(chatService).getAssignedProjectsForCurrentUser();
    }
}
