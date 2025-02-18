package com.ssafy.api.response;

import com.ssafy.db.entity.User;
import com.ssafy.db.entity.UserCharacter;
import com.ssafy.db.entity.UserRankScores;
import com.ssafy.db.entity.UserStats;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@ApiModel("AllCharacterInfoResponse")
public class AllCharacterInfoRes {

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

    @ApiModelProperty(name = "아바타", example = "2-3-3")
    private String avatar;

    @ApiModelProperty(name = "레벨", example = "2")
    private short level;

    @ApiModelProperty(name = "경험치", example = "88")
    private int experience;

    @ApiModelProperty(name = "유저 스탯")
    private UserStats userStats;

    @ApiModelProperty(name = "유저 랭크")
    private String userRank;

    // static factory method to convert from UserInfo and UserCharacter
    public static AllCharacterInfoRes of(User userInfo, UserCharacter userCharacter) {
        AllCharacterInfoRes res = new AllCharacterInfoRes();
        res.setUserName(userInfo.getUserName());
        res.setUserEmail(userInfo.getUserEmail());
        res.setPhoneNumber(userInfo.getPhoneNumber());
        res.setUserNickname(userCharacter.getUserNickname());
        res.setGender(userCharacter.getGender());
        res.setAvatar(userCharacter.getAvatar());
        res.setLevel(userCharacter.getUserLevel());
        res.setExperience(userCharacter.getUserExperience());
        res.setUserRank(userCharacter.getUserRank());
        return res;
    }
}