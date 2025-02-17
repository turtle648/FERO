package com.ssafy.api.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.api.service.MatchingService;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class MatchingWebSocketHandler extends TextWebSocketHandler {
    private final Set<WebSocketSession> sessions = ConcurrentHashMap.newKeySet();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final MatchingService matchingService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.add(session);
        log.info("👌 LOG : New WebSocket connnection established: {}", session.getId());
        log.info("Current session attributes: {}", session.getAttributes());
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        log.info("💬 Received message from session {}: {}", session.getId(), payload);

        try {
            JsonNode jsonNode = objectMapper.readTree(payload);
            String type = jsonNode.path("type").asText();
            log.info("Message type: {}", type);

            if ("ENTER_WAITING_ROOM".equals(type)) {
                String userToken = jsonNode.path("userToken").asText();
                JsonNode exerciseTypeNode = jsonNode.path("exerciseType");

                log.info("Processing ENTER_WAITING_ROOM - Token: {}, Exercise node: {}",
                        userToken, exerciseTypeNode);

                if (userToken == null || userToken.isEmpty()) {
                    log.error("❌ Missing or empty userToken");
                    return;
                }

                Long exerciseType;
                if (exerciseTypeNode.isNumber()) {
                    exerciseType = exerciseTypeNode.asLong();
                } else if (exerciseTypeNode.isTextual()) {
                    exerciseType = Long.parseLong(exerciseTypeNode.asText());
                } else {
                    log.error("❌ Invalid exerciseType format: {}", exerciseTypeNode);
                    return;
                }

                // 세션에 정보 저장
                session.getAttributes().put("userToken", userToken);
                session.getAttributes().put("exerciseType", exerciseType);

                log.info("✅ Successfully stored in session - Token: {}, Exercise Type: {}",
                        userToken, exerciseType);
                log.info("Current session attributes: {}", session.getAttributes());

                // 저장 확인을 위한 응답 메시지
                sendMessage(session, createMessage("ENTERED", "Successfully entered waiting room"));
            }
        } catch (Exception e) {
            log.error("❌ Error processing message: {}", e.getMessage());
            log.error("Raw message payload: {}", payload);
            e.printStackTrace();
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
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        try {
            log.info("Starting cleanup for session: {}", session.getId());
            log.info("Session attributes before cleanup: {}", session.getAttributes());

            String userToken = (String) session.getAttributes().get("userToken");
            Long exerciseType = (Long) session.getAttributes().get("exerciseType");

            if (userToken != null && exerciseType != null) {
                log.info("🧹 Cleaning up Redis data for user: {}", userToken);
                matchingService.leaveWaitingRoom(userToken, exerciseType);
                log.info("✅ Successfully cleaned up Redis data");
            } else {
                log.warn("⚠️ No user data found in session to clean up. Token: {}, Exercise: {}",
                        userToken, exerciseType);
            }
        } catch (Exception e) {
            log.error("❌ Error during cleanup: {}", e.getMessage());
            e.printStackTrace();
        } finally {
            sessions.remove(session);
            log.info("❌ WebSocket connection closed: {}", session.getId());
        }
    }
}
