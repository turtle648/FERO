package com.ssafy.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel("ExerciseLogReq") // 사용자에 운동 기록 추가
public class ExerciseLogReq {
    @ApiModelProperty(value = "운동 시간 (초 단위)", required = true)
    private int exerciseDuration;

    @ApiModelProperty(value = "운동 횟수", required = true)
    private int exerciseCnt;

    @ApiModelProperty(value = "운동 종류 ID", required = true)
    private Long exerciseStatsRatioId;
}
