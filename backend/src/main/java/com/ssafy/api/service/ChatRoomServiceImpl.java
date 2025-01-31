package com.ssafy.api.service;

import com.ssafy.db.entity.ChatRoom;
import com.ssafy.db.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatRoomServiceImpl implements ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    @Override
    public ChatRoom createChatRoom(String userId1, String userId2) {
        // 사용자 ID 정렬
        String firstUserId = userId1.compareTo(userId2) < 0 ? userId1 : userId2;
        String secondUserId = userId1.compareTo(userId2) < 0 ? userId2 : userId1;

        // 중복 방이 있는지 확인
        Optional<ChatRoom> existingRoom = chatRoomRepository.findByUsers(firstUserId, secondUserId);
        if (existingRoom.isPresent()) {
            return existingRoom.get(); // 이미 존재하는 방이면 그대로 반환
        }

        // 새 채팅방 생성
        ChatRoom chatRoom = ChatRoom.builder()
                .userId1(firstUserId)
                .userId2(secondUserId)
                .build();

        return chatRoomRepository.save(chatRoom);
    }

    @Override
    public Optional<ChatRoom> getChatRoom(String userId1, String userId2) {
        // 사용자 ID 정렬
        String firstUserId = userId1.compareTo(userId2) < 0 ? userId1 : userId2;
        String secondUserId = userId1.compareTo(userId2) < 0 ? userId2 : userId1;

        return chatRoomRepository.findByUsers(firstUserId, secondUserId);
    }
}
