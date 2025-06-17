package com.example.oo_backend.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ChatMessageDto {
    private Long messageId;
    private Long senderId;
    private String message;
    private LocalDateTime sentAt;
}

