package com.ssafy.api.service;

import com.ssafy.api.request.ExerciseResultEvent;
import com.ssafy.api.response.RankUpdateRes;
import com.ssafy.db.entity.UserRankScores;

import java.util.List;

/**
 *	캐릭터 관련 비즈니스 로직 처리를 위한 서비스 인터페이스 정의.
 */
public interface UserRankScoresService {
	List<UserRankScores> getAllRankScoresByUserId(String userId);  // 특정 유저의 모든 운동 점수 조회
	UserRankScores getRankScoreByUserIdAndId(String userId, Long Id);  // 특정 운동 점수 조회
	RankUpdateRes updateRankScore(ExerciseResultEvent event);
}