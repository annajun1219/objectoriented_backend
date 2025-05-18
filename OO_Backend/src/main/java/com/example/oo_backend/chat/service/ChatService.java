package com.example.oo_backend.chat.service;

import com.example.oo_backend.chat.dto.ChatRoomResponseDto;
import com.example.oo_backend.chat.entity.ChatMessage;
import com.example.oo_backend.chat.entity.ChatRoom;
import com.example.oo_backend.chat.repository.ChatMessageRepository;
import com.example.oo_backend.chat.repository.ChatRoomRepository;
import com.example.oo_backend.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;

    // 채팅방 목록 조회
    public List<ChatRoomResponseDto> getChatRooms(User loginUser) {
        List<ChatRoom> rooms = chatRoomRepository.findByUser1OrUser2(loginUser, loginUser);

        return rooms.stream()
                .map(room -> {
                    // 상대방 정보 구하기
                    User otherUser = room.getUser1().equals(loginUser) ? room.getUser2() : room.getUser1();

                    // 마지막 메시지 조회
                    Optional<ChatMessage> lastMessageOpt = chatMessageRepository.findTopByRoomOrderBySentAtDesc(room);

                    return ChatRoomResponseDto.builder()
                            .roomId(room.getId())
                            .otherUserId(otherUser.getUserId().toString())
                            .otherUserName(otherUser.getName())
                            .otherUserProfileImage(otherUser.getProfileImage()) // null일 수도 있음
                            .lastMessage(lastMessageOpt.map(ChatMessage::getContent).orElse(""))
                            .lastSentAt(lastMessageOpt.map(ChatMessage::getSentAt).orElse(null))
                            .build();
                })
                .collect(Collectors.toList());
    }
}


