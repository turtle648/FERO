package com.ssafy.db.repository;

import com.ssafy.db.entity.QuestsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface QuestsRepository extends JpaRepository<QuestsEntity, Long> {
    // 사용자의 오늘의 퀘스트 조회
    List<QuestsEntity> findByUserCharacter_User_UserIdAndQuestDate(String userId, LocalDate questDate);

    // 사용자의 월별 퀘스트 성공여부 조회
    List<QuestsEntity> findByUserCharacter_User_UserIdAndQuestDateBetween(String userId, LocalDate startDate, LocalDate endDate);
}
