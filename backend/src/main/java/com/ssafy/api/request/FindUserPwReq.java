package com.ssafy.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@ApiModel("FindUserRequest")
public class FindUserPwReq {
    @ApiModelProperty(name = "유저 아이디", example = "ans")
    private String userId;
    @ApiModelProperty(name = "유저 이메일", example = "ans@test.com")
    private String userEmail;
}

