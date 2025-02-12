package com.ssafy.api.handler;

import com.ssafy.api.request.MatchSuccessEvent;
import com.ssafy.common.model.Message;
import com.ssafy.common.util.RTCUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


@Slf4j
public class SignalingHandler extends TextWebSocketHandler {
    // 어떤 방에 어떤 유저가 들어있는지 저장 -> { 방번호 : [ { id : userUUID1 }, { id: userUUID2 }, …], ... }
    private final Map<String, List<Map<String, String>>> roomInfo = new HashMap<>();

    // userUUID 기준 어떤 방에 들어있는지 저장 -> { userUUID1 : 방번호, userUUID2 : 방번호, ... }
    private final Map<String, String> userInfo = new HashMap<>();

    // 세션 정보 저장 -> { userUUID1 : 세션객체, userUUID2 : 세션객체, ... }
    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final Map<String, String> tokenWithUid = new HashMap<>();

    // 방의 최대 인원수
    private static final int MAXIMUM = 2;

    // 시그널링에 사용되는 메시지 타입 :
    // SDP Offer 메시지
    private static final String MSG_TYPE_OFFER = "offer";
    // SDP Answer 메시지
    private static final String MSG_TYPE_ANSWER = "answer";
    // 새로운 ICE Candidate 메시지
    private static final String MSG_TYPE_CANDIDATE = "candidate";
    // 방 입장 메시지
    private static final String MSG_TYPE_AUTH = "auth";

    // 웹소켓 연결 시
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        log.info(">>> [ws] 클라이언트 접속 : 세션 - {}", session);
    }

//    @Scheduled(fixedRate = 1000)
//    public void monitorWebSocketState() {
//        log.info("=== WebSocket Status ===");
//        log.info("Sessions: {}", sessions);
//        log.info("Token: {}", tokenWithUid);
//        log.info("=====================");
//    }

    @EventListener
    public void matchResult(MatchSuccessEvent event) {
        log.info(">>> ❤️ [ws] 이벤트 리스너 동작!: {}", event);
        String user1 = event.getUserToken1();
        String user2 = event.getUserToken2();
        String roomId = user1 + user2;

        try {
            joinRoom(user1, roomId);
            joinRoom(user2, roomId);
        } catch (Exception e) {
            log.error(">>> ☢️ [ws] 매칭 후 방 조인하면서 에러 발생: {}", e.getMessage());
        }
    }


    public void joinRoom(String token, String roomId) throws Exception {
        String userUUID = tokenWithUid.get(token);
        log.info(">>> [ws] {} 가 #{}번 방에 들어감", userUUID, roomId);

        WebSocketSession session = sessions.get(userUUID);

        // 방이 기존에 생성되어 있다면
        if (roomInfo.containsKey(roomId)) {

            // 현재 입장하려는 방에 있는 인원수
            int currentRoomLength = roomInfo.get(roomId).size();

            // 인원수가 꽉 찼다면 돌아간다
            if (currentRoomLength == MAXIMUM) {

                // 해당 유저에게 방이 꽉 찼다는 메시지를 보내준다
                session.sendMessage(new TextMessage(RTCUtil.getString(Message.builder()
                        .type("room_full")
                        .sender(userUUID).build())));
                return;
            }

            // 여분의 자리가 있다면 해당 방 배열에 추가
            Map<String, String> userDetail = new HashMap<>();
            userDetail.put("id", userUUID);
            roomInfo.get(roomId).add(userDetail);
            log.info(">>> [ws] #{}번 방의 유저들 {}", roomId, roomInfo.get(roomId));

        } else {
            // 방이 존재하지 않는다면 값을 생성하고 추가
            Map<String, String> userDetail = new HashMap<>();
            userDetail.put("id", userUUID);
            List<Map<String, String>> newRoom = new ArrayList<>();
            newRoom.add(userDetail);
            roomInfo.put(roomId, newRoom);
        }

        // 세션 저장, user 정보 저장 -> 방 입장
        sessions.put(userUUID, session);
        userInfo.put(userUUID, roomId);


        // 해당 방에 다른 유저가 있었다면 offer-answer 를 위해 유저 리스트를 만들어 클라이언트에 전달

        // roomInfo = { 방번호 : [ { id : userUUID1 }, { id: userUUID2 }, …], 방번호 : [ { id : userUUID3 }, { id: userUUID4 }, …], ... }
        // originRoomUser -> 본인을 제외한 해당 방의 다른 유저들
        List<Map<String, String>> originRoomUser = new ArrayList<>();
        for (Map<String, String> userDetail : roomInfo.get(roomId)) {

            // userUUID 가 본인과 같지 않다면 list 에 추가
            if (!(userDetail.get("id").equals(userUUID))) {
                Map<String, String> userMap = new HashMap<>();
                userMap.put("id", userDetail.get("id"));
                originRoomUser.add(userMap);
            }
        }

        log.info(">>> [ws] 본인 {} 을 제외한 #{}번 방의 다른 유저들 {}", userUUID, roomId, originRoomUser);

        // all_users 라는 타입으로 메시지 전달
        session.sendMessage(new TextMessage(RTCUtil.getString(Message.builder()
                .type("all_users")
                .allUsers(originRoomUser)
                .sender(userUUID).build())));

    }


    // 양방향 데이터 통신
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage textMessage) throws Exception {
        try {
            // 웹 소켓으로부터 전달받은 메시지를 deserialization(JSON -> Java Object)
            Message message = RTCUtil.getObject(textMessage.getPayload());
            log.info(">>> [ws] 시작!!! 세션 객체 {}", session);

            // 유저 uuid 와 roomID 를 저장
            String userUUID = session.getId(); // 유저 uuid
            String roomId = message.getRoom(); // roomId
            log.info(">>> [ws] 메시지 타입 {}, 보낸 사람 {}", message.getType(), userUUID);

            // 메시지 타입에 따라서 서버에서 하는 역할이 달라진다
            switch (message.getType()) {

                // 클라이언트에게서 받은 메시지 타입에 따른 signal 프로세스
                case MSG_TYPE_OFFER:
                case MSG_TYPE_ANSWER:
                case MSG_TYPE_CANDIDATE:

                    // 전달받은 메시지로부터 candidate, sdp, receiver 를 저장
                    Object candidate = message.getCandidate();
                    Object sdp = message.getSdp();
                    String receiver = message.getReceiver();   // 클라이언트에서 보내주는 1명의 receiver
                    log.info(">>> [ws] receiver {}", receiver);

                    // sessions 에서 receiver 를 찾아 메시지 전달
                    sessions.values().forEach(s -> {
                        try {
                            if(s.getId().equals(receiver)) {
                                s.sendMessage(new TextMessage(RTCUtil.getString(Message.builder()
                                        .type(message.getType())
                                        .sdp(sdp)
                                        .candidate(candidate)
                                        .sender(userUUID)
                                        .receiver(receiver).build())));
                            }
                        }
                        catch (Exception e) {
                            log.info(">>> 에러 발생 : offer, candidate, answer 메시지 전달 실패 {}", e.getMessage());
                        }
                    });
                    break;
                case MSG_TYPE_AUTH:
                    sessions.put(session.getId(), session);
                    tokenWithUid.put(message.getAuth(), session.getId());
                    log.info("📜 사용자 세션 등록 - session: {}, token: {}", session, message.getAuth());
                    break;
                // 메시지 타입이 잘못되었을 경우
                default:
                    log.info(">>> [ws] 잘못된 메시지 타입 {}", message.getType());
            }
        } catch (IOException e) {
            log.info(">>> 에러 발생 : 양방향 데이터 통신 실패 {}", e.getMessage());
        }
    }

    // 소켓 연결 종료
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        log.info(">>> [ws] 클라이언트 접속 해제 : 세션 - {}, 상태 - {}", session, status);

        // 유저 uuid 와 roomID 를 저장
        String userUUID = session.getId(); // 유저 uuid
        String roomId = userInfo.get(userUUID); // roomId

        if (roomId != null && userUUID != null) {
            // 1. Remove user from sessions
            sessions.remove(userUUID);

            // 2. Remove user from userInfo
            userInfo.remove(userUUID);

            // 3. Remove user from roomInfo
            if (roomInfo.containsKey(roomId)) {
                // Remove the user from the room
                roomInfo.get(roomId).removeIf(userMap -> userMap.get("id").equals(userUUID));

                // If the room is empty after removing the user, remove the room entirely
                if (roomInfo.get(roomId).isEmpty()) {
                    roomInfo.remove(roomId);
                    log.info(">>> [ws] 빈 방이어서 #{}번 방 삭제 완료", roomId);
                }
            }

            // 4. Remove user from tokenWithUid
            tokenWithUid.entrySet().removeIf(entry -> entry.getValue().equals(userUUID));

            // 5. Notify other users in the room about the exit
            sessions.values().forEach(s -> {
                try {
                    if (!s.getId().equals(userUUID)) {
                        s.sendMessage(new TextMessage(RTCUtil.getString(Message.builder()
                                .type("user_exit")
                                .sender(userUUID)
                                .build())));
                    }
                } catch (Exception e) {
                    log.error(">>> 에러 발생 : user_exit 메시지 전달 실패 {}", e.getMessage());
                }
            });

            log.info(">>> [ws] #{}번 방에서 {} 삭제 완료", roomId, userUUID);
            if (roomInfo.containsKey(roomId)) {
                log.info(">>> [ws] #{}번 방에 남은 유저 {}", roomId, roomInfo.get(roomId));
            }
        } else {
            log.warn(">>> [ws] 유저 정보를 찾을 수 없음 : {}", userUUID);
        }
    }

    // 소켓 통신 에러
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        log.info(">>> 에러 발생 : 소켓 통신 에러 {}", exception.getMessage());
    }
}
