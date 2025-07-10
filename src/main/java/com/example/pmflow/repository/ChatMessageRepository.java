package com.example.pmflow.repository;

import com.example.pmflow.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    // 🔹 Get private messages for a specific task between two users
    List<ChatMessage> findBySenderIdAndReceiverIdAndProjectIdAndTaskIdOrderByTimestampAsc(
        Long senderId, Long receiverId, Long projectId, Long taskId
    );

    // 🔹 Get reverse direction (receiver → sender) too
    List<ChatMessage> findByReceiverIdAndSenderIdAndProjectIdAndTaskIdOrderByTimestampAsc(
        Long receiverId, Long senderId, Long projectId, Long taskId
    );

    // 🔹 Get all group messages for a project
    List<ChatMessage> findByProjectIdAndIsGroupTrueOrderByTimestampAsc(Long projectId);
}
