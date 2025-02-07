package com.ssafy.api.service;

import com.ssafy.db.entity.UserCharacter;
import com.ssafy.db.entity.UserRankScores;
import com.ssafy.db.entity.UserStats;

import java.util.List;

/**
 *	캐릭터 관련 비즈니스 로직 처리를 위한 서비스 인터페이스 정의.
 */
public interface UserRankScoresService {
	List<UserRankScores> getAllRankScoresByUserId(String userId);  // 특정 유저의 모든 운동 점수 조회
	UserRankScores getRankScoreByUserIdAndId(String userId, Long Id);  // 특정 운동 점수 조회
	void updateRankScore(String winnerId, String loserId, Long exerceseId);
}