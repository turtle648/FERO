package com.ssafy.api.service;

import com.ssafy.api.response.MonthlyQuestsStatusRes;
import com.ssafy.api.response.QuestsRes;
import com.ssafy.db.entity.QuestsEntity;
import com.ssafy.db.repository.QuestsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestsService {
    private final QuestsRepository questsRepository;

    public Optional<List<MonthlyQuestsStatusRes>> getMonthlyQuestStatus(String userId, int year, int month) {
        // 해당 월의 시작일과 마지막일 계산
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.plusMonths(1).minusDays(1);

        // 해당 기간의 퀘스트 조회
        List<QuestsEntity> monthlyQuests = questsRepository.findByUserCharacter_User_UserIdAndQuestDateBetween(
                userId, startDate, endDate
        );

        if (monthlyQuests.isEmpty()) {
            return Optional.empty();
        }

        // 응답 데이터 생성
        List<MonthlyQuestsStatusRes> response = monthlyQuests.stream()
                .map(quest -> MonthlyQuestsStatusRes.builder()
                        .day(quest.getQuestDate().getDayOfMonth())
                        .isCompleted(quest.getIsCompleted())
                        .build())
                .collect(Collectors.toList());

        return Optional.of(response);
    }

    public Optional<List<QuestsRes>> getTodayQuest(String userId, LocalDate date) {
        List<QuestsEntity> todayQuests = questsRepository.findByUserCharacter_User_UserIdAndQuestDate(userId, date);
        if (todayQuests.isEmpty()) {
            return Optional.empty();
        }

        List<QuestsRes> responses = todayQuests.stream()
                .map(QuestsRes::from)
                .collect(Collectors.toList());

        return Optional.of(responses);
    }


}
