package com.ssafy.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("ChatRoomCreateRequest") // 채팅방 생성을 위한 요청 객체
public class ChatRoomCreateReq {
    @ApiModelProperty(name = "유저 1 ID", example = "user123")
    private String userId1;

    @ApiModelProperty(name = "유저 2 ID", example = "user456")
    private String userId2;
}
