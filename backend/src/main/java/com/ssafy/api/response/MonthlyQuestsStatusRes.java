package com.ssafy.api.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("MonthlyQuestsSuccessRes")
public class MonthlyQuestsStatusRes {
    @ApiModelProperty(value = "일자")
    private Integer day;

    @ApiModelProperty(value = "퀘스트 달성 여부")
    private Boolean isCompleted;
}
