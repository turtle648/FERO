package com.ssafy.api.response;

import com.ssafy.db.entity.QuestsEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.time.LocalTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("QuestResponse")
public class QuestsRes {
    @ApiModelProperty(value = "운동 타입 ID")
    private Long exerciseId;

    @ApiModelProperty(value = "운동 이름")
    private String exerciseName;

    @ApiModelProperty(value = "목표 운동 횟수")
    private Integer exerciseCnt;

    @ApiModelProperty(value = "현재까지 수행한 운동 횟수")
    private Integer realCnt;

    @ApiModelProperty(value = "퀘스트 달성 여부")
    private Boolean isCompleted;

    @ApiModelProperty(value = "마지막 업데이트 시간")
    private LocalTime questTime;

    @ApiModelProperty(value = "퀘스트 메시지")
    private String message;

    public static QuestsRes from(QuestsEntity quest) {
        return QuestsRes.builder()
                .exerciseId(quest.getExercise().getId())
                .exerciseName(quest.getExercise().getExerciseType())
                .exerciseCnt(quest.getExerciseCnt())
                .realCnt(quest.getRealCnt())
                .isCompleted(quest.getIsCompleted())
                .questTime(quest.getQuestTime())
                .message(quest.getMessage())
                .build();
    }
}
