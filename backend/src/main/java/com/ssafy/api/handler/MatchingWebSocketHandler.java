package com.ssafy.api.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class MatchingWebSocketHandler extends TextWebSocketHandler {
    private final Set<WebSocketSession> sessions = ConcurrentHashMap.newKeySet();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.add(session);
        log.info("LOG : New WebSocket connnection established: {}", session.getId());
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();

        // 메세지 처리 로직
        JsonNode jsonNode = objectMapper.readTree(payload);
        String type = jsonNode.get("type").asText();

        switch (type) {
            case "ENTER_WAITING_ROOM":
                handleEnterWaitingRoom(session, jsonNode);
                break;
            case "LEAVE_WAITING_ROOM":
                handleLeaveWaitingRoom(session, jsonNode);
                break;
            default:
                log.warn("Unknown message type: {}", type);
        }
    }

    private void handleEnterWaitingRoom(WebSocketSession session, JsonNode jsonNode) throws IOException {
        String userId = jsonNode.get("userId").asText();
        String exerciseType = jsonNode.get("exerciseType").asText();

        // 대기실 입장 처리
        session.getAttributes().put("userId", userId);
        session.getAttributes().put("exerciseType", exerciseType);

        // 입장 확인 메시지 전송
        sendMessage(session, createMessage("ENTERED", "대기실 입장 완료"));
    }

    private void handleLeaveWaitingRoom(WebSocketSession session, JsonNode jsonNode) throws IOException {
        // 대기실 퇴장 처리
        String userId = (String) session.getAttributes().get("userId");
        String exerciseType = (String) session.getAttributes().get("exerciseType");

        sendMessage(session, createMessage("LEFT", "대기실 퇴장 완료"));
    }

    private void sendMessage(WebSocketSession session, String message) throws IOException {
        session.sendMessage(new TextMessage(message));
    }

    private String createMessage(String type, String content) throws JsonProcessingException {
        Map<String, String> messageMap = new HashMap<>();
        messageMap.put("type", type);
        messageMap.put("content", content);
        return objectMapper.writeValueAsString(messageMap);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
        log.info("LOG : WebSocket connection closed: {}", session.getId());
    }
}
