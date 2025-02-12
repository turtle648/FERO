package com.ssafy.db.repository;

import com.ssafy.db.entity.GameResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GameResultRepository extends JpaRepository<GameResult, Long> {

    // 특정 유저의 경기 전적 조회 (최근 경기순)
    List<GameResult> findByUserIdOrderByCreatedAtDesc(String userId);

    // 특정 경기 ID로 모든 기록 조회 (디버깅용)
    List<GameResult> findByGameId(String gameId);

    // 마지막 매칭id 가져오기
    @Query("SELECT MAX(g.gameId) FROM GameResult g")
    Optional<Long> findLatestGameId();
}
