package com.ssafy.api.handler;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.HashMap;
import java.util.Map;

@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final Map<String, WebSocketSession> sessions = new HashMap<>();  // 채팅방에 연결된 세션 관리

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // 연결된 사용자의 채팅방 ID
        String roomId = (String) session.getAttributes().get("roomId");
        sessions.put(roomId, session);  // 세션을 관리하는 맵에 저장
        System.out.println("User connected to room: " + roomId);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String roomId = (String) session.getAttributes().get("roomId");

        // 메시지를 채팅방에 연결된 모든 클라이언트에게 전달
        for (WebSocketSession webSocketSession : sessions.values()) {
            if (webSocketSession.isOpen()) {
                webSocketSession.sendMessage(new TextMessage(message.getPayload()));
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String roomId = (String) session.getAttributes().get("roomId");
        sessions.remove(roomId);  // 연결이 끊어진 세션 제거
        System.out.println("User disconnected from room: " + roomId);
    }
}
