package com.ssafy.api.service;

import com.ssafy.db.entity.ChatRoom;
import java.util.Optional;

public interface ChatRoomService {
    ChatRoom createChatRoom(String userId1, String userId2);
    Optional<ChatRoom> getChatRoom(String userId1, String userId2);
}