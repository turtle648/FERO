package com.ssafy.api.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
public class ExerciseLogSearchReq { // 운동 기록 조회
    @ApiModelProperty(value = "운동 종류 ID", required = false)
    private Long exerciseStatsRatioId;

    @ApiModelProperty(value = "조회 날짜", required = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate searchDate;
}
