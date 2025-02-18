package com.ssafy.api.service;

import com.ssafy.db.entity.User;
import com.ssafy.db.entity.UserCharacter;
import com.ssafy.db.entity.UserStats;
import com.ssafy.db.entity.UserStatsHistory;
import com.ssafy.db.repository.UserRepository;
import com.ssafy.db.repository.UserStatsHistoryRepository;
import com.ssafy.db.repository.UserStatsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service("userStatsService")
public class UserStatsServiceImpl implements UserStatsService {

    private final UserStatsHistoryRepository userStatsHistoryRepository;
    private final UserStatsRepository userStatsRepository;
    private final UserRepository userRepository;

    @Override
    public UserStats getUserStats(String userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("유저 정보가 없습니다."));

        return userStatsRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("해당 캐릭터 정보를 찾을 수 없습니다."));
    }


    public List<Map<String, Object>> getMonthlyStatsHistory(Long userCharacterId, int year, int month) {
        // 해당 월의 시작일과 종료일 계산
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        // 히스토리 데이터 조회
        List<UserStatsHistory> histories = userStatsHistoryRepository.findByUserCharacterIdAndStatsDateBetweenOrderByStatsDateAsc(
                userCharacterId, startDate, endDate);

        // 응답 형식에 맞게 변환
        return histories.stream()
                .map(history -> {
                    Map<String, Object> dailyStats = new HashMap<>();
                    dailyStats.put("date", history.getStatsDate().getDayOfMonth());
                    // 스탯을 순서대로 배열로 저장
                    List<Integer> status = Arrays.asList(
                            history.getArmsStats(),
                            history.getLegsStats(),
                            history.getChestStats(),
                            history.getAbsStats(),
                            history.getBackStats(),
                            history.getStaminaStats()
                    );
                    dailyStats.put("status", status);
                    return dailyStats;
                })
                .collect(Collectors.toList());
    }

}
