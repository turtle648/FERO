package com.ssafy.api.response;

import com.ssafy.db.entity.ChatRoom;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("ChatRoomResponse")
public class ChatRoomRes {
    @ApiModelProperty(name = "채팅방 ID", example = "1")
    private Long roomId;

    public static ChatRoomRes of(ChatRoom chatRoom) {
        ChatRoomRes res = new ChatRoomRes();
        res.setRoomId(chatRoom.getId());
        return res;
    }
}
