package com.ssafy.api.service;

import com.ssafy.api.response.GetTutorialRes;
import com.ssafy.db.entity.UserTutorialProgress;

import java.util.List;

public interface UserTutorialProgressService {

    List<GetTutorialRes> getTutorials(String userId); // 튜토리얼 조회

    boolean isTutorialCompleted(String userId, Long tutorialId); // 특정 튜토리얼 완료 여부 확인

    void completeTutorial(String userId, Long tutorialId); // 튜토리얼 완료 처리

}
