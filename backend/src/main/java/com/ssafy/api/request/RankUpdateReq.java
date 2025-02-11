package com.ssafy.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("RankUpdateReq")
public class RankUpdateReq {

    @ApiModelProperty(name = "승리한 유저의 Id")
    private String winnerId;

    @ApiModelProperty(name = "패배한 유저의 Id")
    private String loserId;

    @ApiModelProperty(name = "운동에 대한 Id")
    private Long exerciseId;
}

