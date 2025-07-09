package com.example.pmflow.controller;

import com.example.pmflow.dto.ChatRequestDTO;
import com.example.pmflow.dto.ChatSummaryDTO;
import com.example.pmflow.dto.MemberProjectDTO;
import com.example.pmflow.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

    // 1. Send Private Message
    @PostMapping("/private/sender/{senderId}/receiver/{receiverId}/project/{projectId}/task/{taskId}")
    public ResponseEntity<ChatSummaryDTO> sendPrivateMessage(
            @PathVariable Long senderId,
            @PathVariable Long receiverId,
            @PathVariable Long projectId,
            @PathVariable Long taskId,
            @RequestBody ChatRequestDTO request
    ) {
        return ResponseEntity.ok(chatService.sendPrivateMessage(senderId, receiverId, projectId, taskId, request));
    }

    // 2. Send Group Message
    @PostMapping("/group/sender/{senderId}/project/{projectId}")
    public ResponseEntity<ChatSummaryDTO> sendGroupMessage(
            @PathVariable Long senderId,
            @PathVariable Long projectId,
            @RequestBody ChatRequestDTO request
    ) {
        return ResponseEntity.ok(chatService.sendGroupMessage(senderId, projectId, request));
    }

    // 3. Get Private Chat Summary
    @GetMapping("/private/sender/{senderId}/receiver/{receiverId}/project/{projectId}/task/{taskId}")
    public ResponseEntity<List<ChatSummaryDTO>> getPrivateChatSummary(
            @PathVariable Long senderId,
            @PathVariable Long receiverId,
            @PathVariable Long projectId,
            @PathVariable Long taskId
    ) {
        return ResponseEntity.ok(chatService.getPrivateChatSummary(senderId, receiverId, projectId, taskId));
    }

    // 4. Get Group Chat Summary
    @GetMapping("/group/project/{projectId}")
    public ResponseEntity<List<ChatSummaryDTO>> getGroupChatSummary(@PathVariable Long projectId) {
        return ResponseEntity.ok(chatService.getGroupChatSummary(projectId));
    }

    // 5. Deeya's Feature: Get Assigned Project Name and Status for Current User
    @GetMapping("/assigned_projects")
    public ResponseEntity<List<MemberProjectDTO>> getAssignedProjects() {
        return ResponseEntity.ok(chatService.getAssignedProjectsForCurrentUser());
    }
} 
