package com.ssafy.api.response;

import com.ssafy.db.entity.User;
import com.ssafy.db.entity.UserCharacter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("UserInfoResponse")
public class UserInfoRes {
    @ApiModelProperty(name = "사용자 이름", example = "John Doe")
    private String userName;

    @ApiModelProperty(name = "사용자 이메일", example = "johndoe@example.com")
    private String userEmail;

    @ApiModelProperty(name = "사용자 전화번호", example = "01012345678")
    private String phoneNumber;

    @ApiModelProperty(name = "사용자 닉네임", example = "johnny")
    private String userNickname;

    @ApiModelProperty(name = "성별", example = "M")
    private char gender;

    // static factory method to convert from UserInfo and UserCharacter
    public static UserInfoRes of(User userInfo, UserCharacter userCharacter) {
        UserInfoRes res = new UserInfoRes();
        res.setUserName(userInfo.getUserName());
        res.setUserEmail(userInfo.getUserEmail());
        res.setPhoneNumber(userInfo.getPhoneNumber());
        res.setUserNickname(userCharacter.getUserNickname());
        res.setGender(userCharacter.getGender());
        return res;
    }
}