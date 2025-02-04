package com.ssafy.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("VideoRoomRequest")
public class VideoRoomReq {
    @ApiModelProperty(name = "방 이름")
    private String roomName;
    @ApiModelProperty(name = "참여자 이름")
    private String participantName;
}
