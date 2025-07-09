package com.example.pmflow.dto;

import java.time.LocalDateTime;

public class ChatSummaryDTO {
    private Long senderId;
    private String senderName;
    private String content;
    private LocalDateTime timestamp;

    public ChatSummaryDTO() {}

    public ChatSummaryDTO(Long senderId, String senderName, String content, LocalDateTime timestamp) {
        this.senderId = senderId;
        this.senderName = senderName;
        this.content = content;
        this.timestamp = timestamp;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
