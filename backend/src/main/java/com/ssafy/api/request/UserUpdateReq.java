package com.ssafy.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("UserUpdateReq")
public class UserUpdateReq {

    @ApiModelProperty(name = "유저 Email", example = "수정할 때만 작성")
    private String userEmail;

    @ApiModelProperty(name = "유저 Phone number", example = "수정할 때만 작성")
    private String phoneNumber;

    @ApiModelProperty(name = "유저 Nickname", example = "수정할 때만 작성")
    private String userNickname;
}

