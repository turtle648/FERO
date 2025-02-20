package com.ssafy.api.request;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@ApiModel("EdnGameReq")
public class EndGameReq {
    private String userToken;
    private String opponentToken;
    private String gameId;
    private String remainTime;
}
