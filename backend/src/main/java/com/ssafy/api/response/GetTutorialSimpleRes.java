package com.ssafy.api.response;

import com.ssafy.db.entity.UserTutorialProgress;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetTutorialSimpleRes {
    private Long tutorialId;  // tutorialType의 id만 추출
    private boolean isCompleted;

    // 엔티티 → DTO 변환하는 static 메서드 추가
    public static GetTutorialSimpleRes fromEntity(UserTutorialProgress tutorialProgress) {
        return new GetTutorialSimpleRes(
            tutorialProgress.getTutorialType().getId(),  // tutorialType의 id만 가져오기
            tutorialProgress.getIsCompleted()
        );
    }
}