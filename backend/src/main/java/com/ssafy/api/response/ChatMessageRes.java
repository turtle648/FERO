package com.ssafy.api.response;

import com.ssafy.db.entity.ChatMessage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("ChatMessageResponse")
public class ChatMessageRes {
    @ApiModelProperty(name = "메시지 ID", example = "1")
    private Long messageId;

    @ApiModelProperty(name = "채팅방 ID", example = "1")
    private Long chatRoomId;

    @ApiModelProperty(name = "보낸 사람 ID", example = "user123")
    private String senderId;

    @ApiModelProperty(name = "메시지 내용", example = "안녕하세요!")
    private String message;

    @ApiModelProperty(name = "전송 시간", example = "2025-01-31T16:11:22")
    private String sendAt;

    public static ChatMessageRes of(ChatMessage chatMessage) {
        ChatMessageRes res = new ChatMessageRes();
        res.setMessageId(chatMessage.getId());
        res.setChatRoomId(chatMessage.getChatRoom().getId());
        res.setSenderId(chatMessage.getSenderId());
        res.setMessage(chatMessage.getMessage());
        res.setSendAt(chatMessage.getSentAt().toString());
        return res;
    }
}
