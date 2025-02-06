package com.ssafy.api.service;

import com.ssafy.api.request.ExerciseLogReq;
import com.ssafy.api.request.ExerciseLogSearchReq;
import com.ssafy.api.response.ExerciseLogRes;
import com.ssafy.api.response.ExerciseStatsRatioRes;
import com.ssafy.db.entity.*;
import com.ssafy.db.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ExerciseLogServiceImpl implements ExerciseLogService {
    private final ExerciseStatsRatioRepository exerciseStatsRatioRepository;
    private final ExerciseLogRepository exerciseLogRepository;
    private final UserStatsRepository userStatsRepository;
    private final UserCharacterRepository userCharacterRepository;

    @Override
    public ExerciseStatsRatioRes getStatsByExerciseLog(Long exerciseStatsRatioId) {
        return exerciseStatsRatioRepository.findById(exerciseStatsRatioId)
                .map(ExerciseStatsRatioRes::of)
                .orElseThrow(() -> new RuntimeException("해당 ID의 운동 스탯을 찾을 수 없습니다."));
    }

    @Override
    public ExerciseLog addExerciseLogAndUpdateStats(String userId, ExerciseLogReq req) {
        // 1. 사용자 캐릭터 조회
        UserCharacter userCharacter = userCharacterRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("사용자 캐릭터를 찾을 수 없습니다."));

        // 2. 운동 종류 존재 여부 확인
        ExerciseStatsRatio exerciseStatsRatio = exerciseStatsRatioRepository
                .findExerciseStatsRatioById(req.getExerciseStatsRatioId())
                .orElseThrow(() -> new EntityNotFoundException("운동 종류를 찾을 수 없습니다."));

        // 3. 운동 기록 저장
        ExerciseLog exerciseLog = new ExerciseLog();
        exerciseLog.setUserCharacter(userCharacter);
        exerciseLog.setExerciseDuration(req.getExerciseDuration());
        exerciseLog.setExerciseCount(req.getExerciseCnt());
        exerciseLog.setExerciseStatsRatio(exerciseStatsRatio);
        exerciseLog.setExerciseDate(LocalDateTime.now());
        ExerciseLog savedLog = exerciseLogRepository.save(exerciseLog);

        System.out.println("사용자 스탯?? : "+userCharacter.getUser());

        // 4. 사용자 스탯 업데이트
        UserStats userStats = userStatsRepository.findByUser(userCharacter.getUser())
                .orElseThrow(() -> new EntityNotFoundException("사용자 스탯을 찾을 수 없습니다."));

        // 5. 운동 종류별 스탯 업데이트 로직
        updateCharacterStats(userStats, exerciseStatsRatio, req.getExerciseCnt());

        return savedLog;
    }
    @Override
    public List<ExerciseLogRes> searchExerciseLog(String userId, ExerciseLogSearchReq request) {
        System.out.println("조회 옵션 : " + request.toString());

        Long exerciseStatsRatioId = request.getExerciseStatsRatioId();
        LocalDateTime startOfDay = null;
        LocalDateTime endOfDay = null;

        if (request.getSearchDate() != null) {
            startOfDay = request.getSearchDate().atStartOfDay();
            endOfDay = request.getSearchDate().atTime(LocalTime.MAX);
        }

        List<ExerciseLog> exerciseLogs;

        if (exerciseStatsRatioId != null && startOfDay != null) {
            // 날짜 + 운동 종류 필터
            exerciseLogs = exerciseLogRepository.findByUserCharacter_User_UserIdAndExerciseDateBetweenAndExerciseStatsRatio_IdOrderByExerciseDateDesc(
                    userId, startOfDay, endOfDay, exerciseStatsRatioId);
        } else if (startOfDay != null) {
            // 날짜만 필터
            exerciseLogs = exerciseLogRepository.findByUserCharacter_User_UserIdAndExerciseDateBetweenOrderByExerciseDateDesc(
                    userId, startOfDay, endOfDay);
        } else if (exerciseStatsRatioId != null) {
            // 운동 종류만 필터
            exerciseLogs = exerciseLogRepository.findByUserCharacter_User_UserIdAndExerciseStatsRatio_IdOrderByExerciseDateDesc(
                    userId, exerciseStatsRatioId);
        } else {
            // 전체 조회
            System.out.println("전체 조회하려는 사용자 정보 : " + userId);
            exerciseLogs = exerciseLogRepository.findByUserCharacter_User_UserIdOrderByExerciseDateDesc(userId);
        }

        return exerciseLogs.stream()
                .map(ExerciseLogRes::from)
                .collect(Collectors.toList());
    }


    private void updateCharacterStats(UserStats userStats, ExerciseStatsRatio exerciseStatsRatio, int exerciseCnt) {
        // 운동 종류별 스탯 반영 로직
        if (exerciseStatsRatio.getArmsRatio() > 0) {
            userStats.setArmsStats((short) Math.min(
                    userStats.getArmsStats() + (exerciseStatsRatio.getArmsRatio() * exerciseCnt),
                    1000 // 최대 스탯 1000으로 제한
            ));
        }
        if (exerciseStatsRatio.getLegsRatio() > 0) {
            userStats.setLegsStats((short) Math.min(
                    userStats.getLegsStats() + (exerciseStatsRatio.getLegsRatio() * exerciseCnt),
                    1000
            ));
        }

        if (exerciseStatsRatio.getChestRatio() > 0) {
            userStats.setChestStats((short) Math.min(
                    userStats.getChestStats() + (exerciseStatsRatio.getChestRatio() * exerciseCnt),
                    1000
            ));
        }

        if (exerciseStatsRatio.getAbsRatio() > 0) {
            userStats.setAbsStats((short) Math.min(
                    userStats.getAbsStats() + (exerciseStatsRatio.getAbsRatio() * exerciseCnt),
                    1000
            ));
        }

        if (exerciseStatsRatio.getBackRatio() > 0) {
            userStats.setBackStats((short) Math.min(
                    userStats.getBackStats() + (exerciseStatsRatio.getBackRatio() * exerciseCnt),
                    1000
            ));
        }

        if (exerciseStatsRatio.getStaminaRatio() > 0) {
            userStats.setStaminaStats((short) Math.min(
                    userStats.getStaminaStats() + (exerciseStatsRatio.getStaminaRatio() * exerciseCnt),
                    1000
            ));
        }

        userStats.setUpdatedAt(LocalDateTime.now());
        userStatsRepository.save(userStats);
    }

}
