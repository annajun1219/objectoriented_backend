package com.example.oo_backend.chat.service;

import com.example.oo_backend.chat.dto.ChatMessageDto;
import com.example.oo_backend.chat.dto.ChatRoomResponseDto;
import com.example.oo_backend.chat.dto.StartChatRequestDto;
import com.example.oo_backend.chat.entity.ChatMessage;
import com.example.oo_backend.chat.entity.ChatRoom;
import com.example.oo_backend.chat.repository.ChatRoomRepository;
import com.example.oo_backend.user.entity.User;
import com.example.oo_backend.user.repository.UserRepository;
import com.example.oo_backend.chat.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl {

    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final ChatMessageRepository chatMessageRepository;

    public ChatRoomResponseDto startChat(StartChatRequestDto requestDto) {
        Optional<ChatRoom> existingRoom = chatRoomRepository
                .findByUser1UserIdAndUser2UserIdAndBookId(
                        requestDto.getBuyerId(), requestDto.getSellerId(), requestDto.getBookId());

        ChatRoom chatRoom = existingRoom.orElseGet(() -> {
            User buyer = userRepository.findById(requestDto.getBuyerId())
                    .orElseThrow(() -> new IllegalArgumentException("구매자 정보를 찾을 수 없습니다."));
            User seller = userRepository.findById(requestDto.getSellerId())
                    .orElseThrow(() -> new IllegalArgumentException("판매자 정보를 찾을 수 없습니다."));

            ChatRoom newRoom = ChatRoom.builder()
                    .user1(buyer)
                    .user2(seller)
                    .bookId(requestDto.getBookId())
                    .build();
            return chatRoomRepository.save(newRoom);
        });

        return ChatRoomResponseDto.builder()
                .roomId(chatRoom.getId())
                .bookId(chatRoom.getBookId())
                .otherUserId(chatRoom.getUser2().getUserId().toString()) // 예시
                .otherUserName(chatRoom.getUser2().getName())
                .otherUserProfileImage(chatRoom.getUser2().getProfileImage())
                .lastMessage("") // 초기 채팅이 없으니까 빈 값
                .lastSentAt(null)
                .build();
    }

    public List<ChatRoomResponseDto> getChatRooms(User loginUser) {

        List<ChatRoom> chatRooms = chatRoomRepository.findByUser1OrUser2(loginUser, loginUser);

        return chatRooms.stream().map(chatRoom -> {
            User otherUser = chatRoom.getUser1().equals(loginUser) ? chatRoom.getUser2() : chatRoom.getUser1();

            return ChatRoomResponseDto.builder()
                    .roomId(chatRoom.getId())
                    .bookId(chatRoom.getBookId())
                    .otherUserId(String.valueOf(otherUser.getUserId()))
                    .otherUserName(otherUser.getName())
                    .otherUserProfileImage(otherUser.getProfileImage())
                    .lastMessage("") // 나중에 메시지 연동 시 넣을 수 있음
                    .lastSentAt(null) // 마지막 메시지 시간도 넣을 수 있음
                    .build();
        }).toList();
    }

    // 메시지 조회
    public List<ChatMessageDto> getChatMessages(Long roomId, Long userId) {
        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방을 찾을 수 없습니다."));

        return chatMessageRepository.findByRoomOrderBySentAtAsc(room).stream()
                .map(msg -> new ChatMessageDto(
                        msg.getId(),
                        msg.getSender().getUserId(),
                        msg.getContent(),
                        msg.getSentAt()
                ))
                .collect(Collectors.toList());
    }

    public void sendMessage(Long roomId, Long userId, String message) {
        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방이 존재하지 않습니다."));

        User sender = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        ChatMessage chatMessage = new ChatMessage(
                room,
                sender,
                message,
                LocalDateTime.now()
        );
        chatMessageRepository.save(chatMessage);
    }



}
