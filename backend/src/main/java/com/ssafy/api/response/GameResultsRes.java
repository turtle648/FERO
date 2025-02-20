package com.ssafy.api.response;

import com.ssafy.db.entity.GameResult;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@ApiModel("GameResultsRes")
@AllArgsConstructor
public class GameResultsRes {
    @ApiModelProperty(name = "사용자 아이디")
    private String userId; // 유저 ID

    @ApiModelProperty(name = "경기 ID")
    private String gameId;

    @ApiModelProperty(name = "경기 시간")
    private Integer duration; // 경기 시간

    @ApiModelProperty(name = "운동 종류")
    private Long exerciseId; // 운동 종류

    @ApiModelProperty(name = "상대방 ID")
    private String opponentId; // 상대방 ID

    @ApiModelProperty(name = "유저 점수")
    private short userScore; // 유저 점수

    @ApiModelProperty(name = "상대 점수")
    private short opponentScore; // 상대 점수

    @ApiModelProperty(name = "승패 결과")
    private GameResult.GameResultType result; // 승패 결과 (WIN, LOSE, DRAW)

    @ApiModelProperty(name = "생성 시간")
    private LocalDateTime createdAt; // 생성 시간
}
