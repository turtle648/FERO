package com.ssafy.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("ChatMessageSendRequest")
public class ChatMessageSendReq {
    @ApiModelProperty(name = "채팅방 ID", example = "1")
    private Long chatRoomId;

    @ApiModelProperty(name = "보낸 사람 ID", example = "user123")
    private String senderId;

    @ApiModelProperty(name = "메시지 내용", example = "안녕하세요!")
    private String message;
}
