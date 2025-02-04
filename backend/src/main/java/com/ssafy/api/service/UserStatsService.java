package com.ssafy.api.service;

import com.ssafy.db.entity.UserCharacter;
import com.ssafy.db.entity.UserStats;

/**
 *	캐릭터 관련 비즈니스 로직 처리를 위한 서비스 인터페이스 정의.
 */
public interface UserStatsService {
	UserStats getUserStats(String userId);
	UserCharacter getUserLevel(String userId);
	UserCharacter getUserRankScore(String userId);
}
