package com.ssafy.api.service;

import com.ssafy.db.entity.ChatMessage;
import com.ssafy.db.entity.ChatRoom;

import java.util.List;

public interface ChatMessageService {
    ChatMessage sendMessage(ChatRoom chatRoom, String senderId, String message);
    List<ChatMessage> getMessages(ChatRoom chatRoom);
}
