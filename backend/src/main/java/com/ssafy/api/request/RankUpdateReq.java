package com.ssafy.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("RankUpdateReq")
public class RankUpdateReq {

    @ApiModelProperty(name = "User1의 Id")
    private String user1Id;

    @ApiModelProperty(name = "User2의 Id")
    private String user2Id;

    @ApiModelProperty(name = "운동에 대한 Id")
    private Long exerciseId;

    @ApiModelProperty(name = "user1 에 대한 승, 패, 무")
    private int result;
}

