package com.ssafy.api.response;


import com.ssafy.db.entity.UserStats;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@ApiModel("SingleModeRes")
public class SingleModeRes {

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

    @ApiModelProperty(name = "나의 스코어", example = "33")
    private Integer userScore;

    @ApiModelProperty(name = "운동 아이디", example = "33")
    private Long exerciseId;
}
