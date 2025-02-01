package com.ssafy.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("UserUpdateRequest")
public class UserUpdateReq {
    @ApiModelProperty(name = "유저 Password")
    String userPassword;
    @ApiModelProperty(name = "유저 Email")
    String userEmail;
    @ApiModelProperty(name = "유저 Phone number")
    String phoneNumber;
    @ApiModelProperty(name = "유저 Nickname")
    String userNickname;
}
