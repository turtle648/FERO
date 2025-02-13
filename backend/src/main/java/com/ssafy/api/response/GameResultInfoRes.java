package com.ssafy.api.response;

import com.ssafy.db.entity.User;
import com.ssafy.db.entity.UserCharacter;
import com.ssafy.db.entity.UserStats;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("GameResultInfoRes")
public class GameResultInfoRes {

    @ApiModelProperty(name = "사용자 아이디", example = "ssafy123")
    private String userId;

    @ApiModelProperty(name = "경기 전 유저 스탯")
    private UserStats beforeStats;

    @ApiModelProperty(name = "경기 후 유저 스탯")
    private UserStats afterStats;

    @ApiModelProperty(name = "유저의 경기 전 레벨", example = "1")
    private short beforeUserLevel;
    
    @ApiModelProperty(name = "유저의 경기 전 경험치", example = "1")
    private Integer beforeUserExperience;

    @ApiModelProperty(name = "유저의 경기 후 레벨", example = "12")
    private short afterUserLevel;
    
    @ApiModelProperty(name = "유저의 경기 후 경험치", example = "17")
    private Integer afterUserExperience;
    
    @ApiModelProperty(name = "상대방 아이디", example = "kim456")
    private String opponentId;

    @ApiModelProperty(name = "나의 스코어", example = "33")
    private Integer userScore;

    @ApiModelProperty(name = "상대방의 스코어", example = "33")
    private Integer opponentScore;
    
    @ApiModelProperty(name = "운동 아이디", example = "33")
    private Long exerciseId;

    @ApiModelProperty(name = "사용자의 이전 랭크점수", example = "33")
    private short beforeRankScore;

    @ApiModelProperty(name = "사용자의 이후 랭크점수", example = "53")
    private short afterRankScore;


    // static factory method to convert from UserInfo and UserCharacter
//    public static GameResultInfoRes of(User userInfo, UserCharacter userCharacter) {
//        GameResultInfoRes res = new GameResultInfoRes();
//        res.setUserName(userInfo.getUserName());
//        res.setUserEmail(userInfo.getUserEmail());
//        res.setPhoneNumber(userInfo.getPhoneNumber());
//        res.setUserNickname(userCharacter.getUserNickname());
//        res.setGender(userCharacter.getGender());
//        res.setAvatar(userCharacter.getAvatar());
//        res.setLevel(userCharacter.getUserLevel());
//        res.setExperience(userCharacter.getUserExperience());
//        return res;
//    }
}