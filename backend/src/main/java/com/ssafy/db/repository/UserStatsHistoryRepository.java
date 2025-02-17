package com.ssafy.db.repository;


import com.ssafy.db.entity.UserStatsHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface UserStatsHistoryRepository extends JpaRepository<UserStatsHistory, Long> {
    List<UserStatsHistory> findByUserCharacterIdAndStatsDateBetweenOrderByStatsDateAsc(
            Long userCharacterId, LocalDate startDate, LocalDate endDate);
}
