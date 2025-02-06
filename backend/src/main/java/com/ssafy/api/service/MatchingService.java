package com.ssafy.api.service;

import com.ssafy.api.request.WaitingUser;
import com.ssafy.api.response.MatchSuccessRes;
import com.ssafy.api.response.MatchTimeoutRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MatchingService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final SimpMessagingTemplate messagingTemplate;

    // Redis Key 상수
    private static final String WAITING_QUEUE = "waiting:queue";
    private static final String SCORE_SORTED_SET = "score:sorted:set";
    private static final String USER_INFO = "user:info";

    // 유저를 대기열에 추가함
    public void enterWaitingRoom(String userId, String exerciseType, int rankScore) {
        WaitingUser waitingUser = new WaitingUser(userId, exerciseType, rankScore, LocalDateTime.now());

        // 운동 타입별 키 생성
        String queueKey = WAITING_QUEUE + exerciseType;
        String sortedKey = SCORE_SORTED_SET + exerciseType;
        String userInfoKey = USER_INFO + exerciseType;

        // 유저 정보 저장
        redisTemplate.opsForHash().put(userInfoKey, userId, waitingUser);
        // 입장 순서 큐에 추가
        redisTemplate.opsForList().rightPush(queueKey, userId);
        // 스코어 정렬셋에 추가
        redisTemplate.opsForZSet().add(sortedKey, userId, rankScore);

        log.info("User {} entered waiting room for {}", userId, exerciseType);
    }

    // 매칭 처리 로직 (스케줄러로 주기적으로 실행할 것)
    @Scheduled(fixedRate = 1000) // 매 초마다 실행함
    public void processMatching() {
        // 모든 운동 타입에 대해 처리함
        Arrays.asList("푸시업", "스쿼트", "런지", "플랭크").forEach(this::processMatchingForExercise);
    }

    private void processMatchingForExercise(String exerciseType) {
        String queueKey = WAITING_QUEUE + exerciseType;
        String sortedSetKey = SCORE_SORTED_SET + exerciseType;
        String userInfoKey = USER_INFO + exerciseType;

        // 큐의 첫 번째 유저 확인
        String userId = (String) redisTemplate.opsForList().index(queueKey, 0);
        if (userId == null) return;

        // 유저 정보 조회
        WaitingUser user = (WaitingUser) redisTemplate.opsForHash().get(userInfoKey, userId);
        if (user == null) {
            redisTemplate.opsForList().remove(queueKey, 1, userId);
            return;
        }

        // 최소 대기시간(30초) 체크
        long waitingSeconds = Duration.between(user.getJoinTime(), LocalDateTime.now()).getSeconds();
        if (waitingSeconds < 30) {
            log.debug("User {} is waiting for {}s (minimum 30s required)", userId, waitingSeconds);
            return;
        }

        // 30초 이상 대기한 유저에 대한 매칭 로직
        Double userScore = redisTemplate.opsForZSet().score(sortedSetKey, userId);
        if (userScore == null) {
            log.error("User {} not found in sorted set", userId);
            return;
        }

        // 랭크 점수 ±100 범위 내의 후보자들 찾기
        Set<String> candidates = redisTemplate.opsForZSet().rangeByScore(
                sortedSetKey,
                userScore - 100,
                userScore + 100
        ).stream().map(Object::toString).collect(Collectors.toSet());

        // 자기 자신 제외
        candidates.remove(userId);

        if (candidates.isEmpty()) {
            // 매칭 가능한 상대가 없으면 큐의 맨 뒤로 이동
            redisTemplate.opsForList().remove(queueKey, 1, userId);
            redisTemplate.opsForList().rightPush(queueKey, userId);
            log.debug("No matching candidates for user {}, moved to back of queue", userId);
            return;
        }

        // 가장 점수 차이가 적은 상대 찾기
        String bestMatch = candidates.stream()
                .min((id1, id2) -> {
                    Double score1 = redisTemplate.opsForZSet().score(sortedSetKey, id1);
                    Double score2 = redisTemplate.opsForZSet().score(sortedSetKey, id2);
                    return Double.compare(
                            Math.abs(userScore - score1),
                            Math.abs(userScore - score2)
                    );
                }).orElse(null);

        if (bestMatch != null) {
            // 매칭 성공 처리
            createMatch(exerciseType, userId, bestMatch);

            // 매칭된 유저들 제거
            removeFromWaitingRoom(exerciseType, userId);
            removeFromWaitingRoom(exerciseType, bestMatch);
        }
    }

    private void createMatch(String exerciseType, String user1, String user2) {
        // 매칭 정보 생성 및 저장 로직
        String matchId = UUID.randomUUID().toString();
        // DB에 매칭 정보 저장

        // WebSocket으로 매칭 성공 알림
        notifyMatchSuccess(user1, user2, matchId);
    }

    private void removeFromWaitingRoom(String exerciseType, String userId) {
        String queueKey = getQueueKey(exerciseType);
        String sortedSetKey = getSortedSetKey(exerciseType);
        String userInfoKey = getUserInfoKey(exerciseType);

        redisTemplate.opsForList().remove(queueKey, 1, userId);
        redisTemplate.opsForZSet().remove(sortedSetKey, userId);
        redisTemplate.opsForHash().delete(userInfoKey, userId);
    }

    private void notifyMatchSuccess(String user1, String user2, String matchId) {
        // 매칭 정보 생성
        MatchSuccessRes matchInfo = createMatchSuccessDTO(user1, user2, matchId);

        // 각 유저에게 WebSocket으로 매칭 성공 메시지 전송
        messagingTemplate.convertAndSend("/topic/match/" + user1, matchInfo);
        messagingTemplate.convertAndSend("/topic/match/" + user2, matchInfo);

        log.info("Match success notification sent to users {} and {}", user1, user2);
    }

    private MatchSuccessRes createMatchSuccessDTO(String user1, String user2, String matchId) {
        return MatchSuccessRes.builder()
                .matchId(matchId)
                .player1Id(user1)
                .player2Id(user2)
                .matchedAt(LocalDateTime.now())
                .build();
    }

    // 5분 타임아웃 체크 (주기적 실행)
    @Scheduled(fixedRate = 30000)  // 30초마다 실행
    public void checkTimeouts() {
        Arrays.asList(EXERCISE_TYPES).forEach(this::checkTimeoutForExercise);
    }

    private void checkTimeoutForExercise(String exerciseType) {
        String queueKey = getQueueKey(exerciseType);
        String userInfoKey = getUserInfoKey(exerciseType);

        // 대기열의 모든 유저 확인
        List<Object> userIds = redisTemplate.opsForList().range(queueKey, 0, -1);
        if (userIds == null || userIds.isEmpty()) return;

        for (Object userId : userIds) {
            WaitingUser user = (WaitingUser) redisTemplate.opsForHash().get(userInfoKey, userId.toString());
            if (user != null && user.isExpired()) {
                // 5분 초과된 유저 제거
                removeFromWaitingRoom(exerciseType, userId.toString());

                // 타임아웃 알림
                notifyTimeout(userId.toString());
            }
        }
    }

    private void notifyTimeout(String userId) {
        MatchTimeoutRes timeoutInfo = MatchTimeoutRes.builder()
                .userId(userId)
                .message("매칭 대기 시간이 초과되었습니다.")
                .timeoutAt(LocalDateTime.now())
                .build();

        messagingTemplate.convertAndSend("/topic/match/" + userId, timeoutInfo);
        log.info("Match timeout notification sent to user {}", userId);
    }

    // Redis Key 생성 유틸리티 메서드들
    private String getQueueKey(String exerciseType) {
        return String.format("%s:waiting:queue", exerciseType.toLowerCase());
    }

    private String getSortedSetKey(String exerciseType) {
        return String.format("%s:score:set", exerciseType.toLowerCase());
    }

    private String getUserInfoKey(String exerciseType) {
        return String.format("%s:user:info", exerciseType.toLowerCase());
    }

}
