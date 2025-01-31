package com.ssafy.api.controller;

import com.ssafy.api.request.ChatRoomCreateReq;
import com.ssafy.api.response.ChatRoomRes;
import com.ssafy.api.service.ChatRoomService;
import com.ssafy.db.entity.ChatRoom;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Api(value = "채팅방 API", tags = {"ChatRoom"})
@RestController
@RequestMapping("/api/v1/chatrooms")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @PostMapping()
    @ApiOperation(value = "채팅방 생성", notes = "두 유저 간 채팅방을 생성합니다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 400, message = "잘못된 요청"),
            @ApiResponse(code = 500, message = "서버 오류")
    })
    public ResponseEntity<ChatRoomRes> createChatRoom(
            @RequestBody @ApiParam(value = "채팅방 생성 정보", required = true) ChatRoomCreateReq request) {

        ChatRoom chatRoom = chatRoomService.createChatRoom(request.getUserId1(), request.getUserId2());
        return ResponseEntity.ok(ChatRoomRes.of(chatRoom));
    }

    @GetMapping("/{userId1}/{userId2}")
    @ApiOperation(value = "채팅방 조회", notes = "두 유저 간 채팅방이 존재하는지 조회합니다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 404, message = "채팅방 없음"),
            @ApiResponse(code = 500, message = "서버 오류")
    })
    public ResponseEntity<ChatRoomRes> getChatRoom(
            @PathVariable String userId1,
            @PathVariable String userId2) {

        Optional<ChatRoom> chatRoom = chatRoomService.getChatRoom(userId1, userId2);
        return chatRoom.map(room -> ResponseEntity.ok(ChatRoomRes.of(room)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
