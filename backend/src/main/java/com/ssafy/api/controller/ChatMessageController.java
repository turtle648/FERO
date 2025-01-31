package com.ssafy.api.controller;

import com.ssafy.api.request.ChatMessageSendReq;
import com.ssafy.api.response.ChatMessageRes;
import com.ssafy.api.service.ChatMessageService;
import com.ssafy.db.entity.ChatMessage;
import com.ssafy.db.entity.ChatRoom;
import com.ssafy.db.repository.ChatRoomRepository;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Api(value = "채팅 메시지 API", tags = {"ChatMessage"})
@RestController
@RequestMapping("/api/v1/chatmessages")
@RequiredArgsConstructor
public class ChatMessageController {

    private final ChatMessageService chatMessageService;
    private final ChatRoomRepository chatRoomRepository;

    @PostMapping()
    @ApiOperation(value = "메시지 전송", notes = "채팅방에 메시지를 전송합니다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 400, message = "잘못된 요청"),
            @ApiResponse(code = 500, message = "서버 오류")
    })
    public ResponseEntity<ChatMessageRes> sendMessage(
            @RequestBody @ApiParam(value = "메시지 정보", required = true) ChatMessageSendReq request) {

        ChatRoom chatRoom = chatRoomRepository.findById(request.getChatRoomId()).orElseThrow();
        ChatMessage chatMessage = chatMessageService.sendMessage(chatRoom, request.getSenderId(), request.getMessage());

        return ResponseEntity.ok(ChatMessageRes.of(chatMessage));
    }

    @GetMapping("/{chatRoomId}")
    @ApiOperation(value = "메시지 조회", notes = "채팅방의 모든 메시지를 조회합니다.")
    public ResponseEntity<List<ChatMessageRes>> getMessages(@PathVariable Long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow();
        List<ChatMessageRes> messages = chatMessageService.getMessages(chatRoom).stream()
                .map(ChatMessageRes::of)
                .collect(Collectors.toList());

        return ResponseEntity.ok(messages);
    }
}
