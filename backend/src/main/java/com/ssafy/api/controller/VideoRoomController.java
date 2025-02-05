//package com.ssafy.api.controller;
//
//import java.util.Map;
//
//import com.ssafy.api.response.VideoRoomRes;
//import com.ssafy.common.model.response.BaseResponseBody;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import io.livekit.server.AccessToken;
//import io.livekit.server.RoomJoin;
//import io.livekit.server.RoomName;
//import io.livekit.server.WebhookReceiver;
//import livekit.LivekitWebhook.WebhookEvent;
//
//@RestController
//@RequestMapping("/api/v1/video-room")
//public class VideoRoomController {
//    @Value("${livekit.api.key}")
//    private String LIVEKIT_API_KEY;
//
//    @Value("${livekit.api.secret}")
//    private String LIVEKIT_API_SECRET;
//
//    /**
//     * @param params JSON object with roomName and participantName
//     * @return JSON object with the JWT token
//     */
//    @PostMapping(value = "/token")
//    public ResponseEntity<?> createToken(@RequestBody Map<String, String> params) {
//        String roomName = params.get("roomName");
//        String participantName = params.get("participantName");
//
//        if (roomName == null || participantName == null) {
//            return ResponseEntity.badRequest().body(BaseResponseBody.of(500, "roomName and participantName are required"));
//        }
//
//        AccessToken token = new AccessToken(LIVEKIT_API_KEY, LIVEKIT_API_SECRET);
//        System.out.println(LIVEKIT_API_KEY + "::::" + LIVEKIT_API_SECRET);
//	token.setName(participantName);
//        token.setIdentity(participantName);
//        token.addGrants(new RoomJoin(true), new RoomName(roomName));
//        return ResponseEntity.status(200).body(VideoRoomRes.of(token.toJwt()));
//    }
//
//    @PostMapping(value = "/livekit/webhook", consumes = "application/webhook+json")
//    public ResponseEntity<String> receiveWebhook(@RequestHeader("Authorization") String authHeader, @RequestBody String body) {
//        WebhookReceiver webhookReceiver = new WebhookReceiver(LIVEKIT_API_KEY, LIVEKIT_API_SECRET);
//        try {
//            WebhookEvent event = webhookReceiver.receive(body, authHeader);
//            System.out.println("LiveKit Webhook: " + event.toString());
//        } catch (Exception e) {
//            System.err.println("Error validating webhook event: " + e.getMessage());
//        }
//        return ResponseEntity.ok("ok");
//    }
//}
//
