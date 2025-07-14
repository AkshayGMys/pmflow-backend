package com.example.pmflow.service;

import com.example.pmflow.dto.ChatRequestDTO;
import com.example.pmflow.dto.ChatResponseDTO;
import com.example.pmflow.dto.ChatSummaryDTO;
import com.example.pmflow.dto.MemberProjectDTO;
import com.example.pmflow.entity.ChatMessage;
import com.example.pmflow.entity.Project;
import com.example.pmflow.entity.Task;
import com.example.pmflow.entity.User;
import com.example.pmflow.repository.ChatMessageRepository;
import com.example.pmflow.repository.ChatProjectRepository;
import com.example.pmflow.repository.ProjectRepository;
import com.example.pmflow.repository.TaskRepository;
import com.example.pmflow.repository.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatService {

    private static final Logger log = LoggerFactory.getLogger(ChatService.class);

    @Autowired
    private ChatMessageRepository chatRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private TaskRepository taskRepo;

    @Autowired
    private ProjectRepository projectRepo;

    @Autowired
    private ChatProjectRepository chatProjectRepo;

    // 1. Send Private Message
    public ChatSummaryDTO sendPrivateMessage(Long senderId, Long receiverId, Long projectId, Long taskId, ChatRequestDTO request) {
        log.info("Sending private message: sender={}, receiver={}, projectId={}, taskId={}", senderId, receiverId, projectId, taskId);

        User sender = userRepo.findById(senderId).orElseThrow(() -> new RuntimeException("Sender not found"));
        User receiver = userRepo.findById(receiverId).orElseThrow(() -> new RuntimeException("Receiver not found"));
        Project project = projectRepo.findById(projectId).orElseThrow(() -> new RuntimeException("Project not found"));
        Task task = taskRepo.findById(taskId).orElseThrow(() -> new RuntimeException("Task not found"));

        ChatMessage message = new ChatMessage();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setProject(project);
        message.setTask(task);
        message.setContent(request.getContent());
        message.setGroup(false);

        ChatMessage saved = chatRepo.save(message);
        log.info("Private message saved with ID: {}", saved.getId());

        return mapToSummaryDTO(saved);
    }

    // 2. Send Group Message
    public ChatSummaryDTO sendGroupMessage(Long senderId, Long projectId, ChatRequestDTO request) {
        log.info("Sending group message: sender={}, projectId={}", senderId, projectId);

        User sender = userRepo.findById(senderId).orElseThrow(() -> new RuntimeException("Sender not found"));
        Project project = projectRepo.findById(projectId).orElseThrow(() -> new RuntimeException("Project not found"));

        ChatMessage message = new ChatMessage();
        message.setSender(sender);
        message.setReceiver(null);
        message.setProject(project);
        message.setTask(null);
        message.setContent(request.getContent());
        message.setGroup(true);

        ChatMessage saved = chatRepo.save(message);
        log.info("Group message saved with ID: {}", saved.getId());

        return mapToSummaryDTO(saved);
    }

    // 3. Get Private Chat Summary Only (strict access-controlled)
    public List<ChatSummaryDTO> getPrivateChatSummary(Long senderId, Long receiverId, Long projectId, Long taskId) {
        Long currentUserId = getCurrentUserId();
        if (!currentUserId.equals(senderId) && !currentUserId.equals(receiverId) && !isAdmin(currentUserId)) {
            throw new SecurityException("You are not authorized to view this private chat.");
        }

        List<ChatMessage> msgs1 = chatRepo.findBySenderIdAndReceiverIdAndProjectIdAndTaskIdOrderByTimestampAsc(senderId, receiverId, projectId, taskId);
        List<ChatMessage> msgs2 = chatRepo.findByReceiverIdAndSenderIdAndProjectIdAndTaskIdOrderByTimestampAsc(senderId, receiverId, projectId, taskId);

        List<ChatMessage> allMsgs = new ArrayList<>();
        allMsgs.addAll(msgs1);
        allMsgs.addAll(msgs2);
        allMsgs.sort(Comparator.comparing(ChatMessage::getTimestamp));

        return allMsgs.stream().map(this::mapToSummaryDTO).collect(Collectors.toList());
    }

    // 4. Get Group Chat Summary Only (strict access-controlled)
    public List<ChatSummaryDTO> getGroupChatSummary(Long projectId) {
        Long currentUserId = getCurrentUserId();
        Project project = projectRepo.findById(projectId).orElseThrow(() -> new RuntimeException("Project not found"));

        boolean isManager = project.getManager().getId().equals(currentUserId);
        boolean isTeamMember = project.getTeamMembers().stream().anyMatch(user -> user.getId().equals(currentUserId));

        if (!isManager && !isTeamMember && !isAdmin(currentUserId)) {
            throw new SecurityException("You are not authorized to view this group chat.");
        }

        List<ChatMessage> messages = chatRepo.findByProjectIdAndIsGroupTrueOrderByTimestampAsc(projectId);
        return messages.stream().map(this::mapToSummaryDTO).collect(Collectors.toList());
    }

    // 5. Assigned Projects (Deeya's Feature)
    public List<MemberProjectDTO> getAssignedProjectsForCurrentUser() {
        Long currentUserId = getCurrentUserId();
        List<Project> projects = chatProjectRepo.findProjectsAssignedToUser(currentUserId);
        return projects.stream()
                .map(p -> new MemberProjectDTO(p.getId(),p.getName(), p.getStatus().toString()))
                .collect(Collectors.toList());
    }

    // Mapper for Summary View
    private ChatSummaryDTO mapToSummaryDTO(ChatMessage msg) {
        return new ChatSummaryDTO(
                msg.getSender().getId(),
                msg.getSender().getFirstName() + " " + msg.getSender().getLastName(),
                msg.getContent(),
                msg.getTimestamp()
        );
    }

    // Get current user
    private Long getCurrentUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails userDetails) {
            String username = userDetails.getUsername();
            return userRepo.findByUsernameOrEmail(username, username)
                    .map(User::getId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
        }
        return null;
    }

    private boolean isAdmin(Long userId) {
        return userRepo.findById(userId)
                .map(user -> user.getRole().name().equals("ADMIN"))
                .orElse(false);
    }
}