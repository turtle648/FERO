package com.ssafy.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.api.request.WaitingUser;
import com.ssafy.db.repository.ExerciseStatsRatioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class MatchingService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final StringRedisTemplate stringRedisTemplate;

    // Redis Key 상수
    private static final String WAITING_QUEUE = "waiting:queue:"; // 입장 순서
    private static final String SCORE_SORTED_SET = "score:sorted:set:"; // 점수 정렬
    private static final String USER_INFO = "user:info:"; // 유저 정보
    private static final String USER_JOIN_TIME = "waiting:expire:"; // 5분 타임아웃 체크 (주기적 실행)
    private final ExerciseStatsRatioRepository exerciseStatsRatioRepository;

    // Redis Key 생성 유틸리티 메서드들
    private String getQueueKey(Long exerciseType) {
        return String.format("waiting:queue%s", exerciseType);
    }
    private String getSortedSetKey(Long exerciseType) {
        return String.format("score:sorted:set%s", exerciseType);
    }
    private String getUserInfoKey(Long exerciseType) {
        return String.format("user:info%s", exerciseType);
    }
    private String getUserJoinTimeKey(Long exerciseType, String userToken) {
        return String.format("waiting:expire:%s:%s", exerciseType, userToken);
    }

    // 1-1. 유저를 대기열에 추가함
    public void enterWaitingRoom(String userToken, Long exerciseType, Short rankScore) {
        WaitingUser waitingUser = new WaitingUser(userToken, exerciseType, rankScore, LocalDateTime.now());

        // 운동 타입별 키 생성
        String queueKey = getQueueKey(exerciseType);
        String sortedKey = getSortedSetKey(exerciseType);
        String userInfoKey = getUserInfoKey(exerciseType);
        String expireKey = getUserJoinTimeKey(exerciseType, userToken);
//        System.out.printf("생성되는 운동 타입 키 : %s, %s, %s\n", queueKey, sortedKey, userInfoKey);

        // 유저 정보 저장
        redisTemplate.opsForHash().put(userInfoKey, userToken, waitingUser);
        // 입장 순서 큐에 추가
        redisTemplate.opsForList().rightPush(queueKey, userToken);
        // 스코어 정렬셋에 추가
        redisTemplate.opsForZSet().add(sortedKey, userToken, (double) rankScore);
        // 입장 시간 TTL 설정 (5분 만료)
        stringRedisTemplate.opsForValue().set(expireKey, "EXPIRED", Duration.ofMinutes(1));

        log.info("✅ {} 사용자가 {} 대기열에 입장 (TTL 설정 완료)", userToken, exerciseType);
        logWaitingRoomStatus(exerciseType);
    }

    // 1-2. 대기방 상태
    private void logWaitingRoomStatus(Long exerciseType) {
        String queueKey = getQueueKey(exerciseType);
        String userInfoKey = getUserInfoKey(exerciseType);

        List<Object> userTokens = redisTemplate.opsForList().range(queueKey, 0, -1);

        ObjectMapper mapper = new ObjectMapper();
        // DateTime 모듈 등록
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        System.out.println("총 큐의 대기 인원: " + (userTokens != null ? userTokens.size() : 0));

        if (userTokens != null && !userTokens.isEmpty()) {
            System.out.println("대기중인 사용자들:");
            for (Object userId : userTokens) {
                Map<Object, Object> userMap = redisTemplate.opsForHash().entries(userInfoKey);
                Object userObj = userMap.get(userId.toString());

                if (userObj != null) {
                    try {
                        WaitingUser user = mapper.convertValue(userObj, WaitingUser.class);
                        long waitingSeconds = Duration.between(user.getJoinTime(), LocalDateTime.now()).getSeconds();
                        System.out.printf("- ID: %s, 점수: %d, 대기시간: %d초%n",
                                user.getUserId(),
                                user.getRankScore(),
                                waitingSeconds
                        );
                    } catch (Exception e) {
                        System.out.println("Error converting user: " + e.getMessage());
                    }
                }
            }
        }
        System.out.println("==========================================\n");
    }

    // 2-1. 대기방 퇴장 메서드 
    public void leaveWaitingRoom(String userToken, Long exerciseType) {
        String userInfoKey = getUserInfoKey(exerciseType);


        // 유저 정보 조회 시도
        Object userObj = redisTemplate.opsForHash().get(userInfoKey, userToken);
        if (userObj == null) {
            log.warn("User {} not found in waiting room {}", userToken, exerciseType);
            return;
        }
        log.info("Removing user {} from waiting room for {}", userToken, exerciseType);
        removeFromRedis(exerciseType, userToken);

    }
    
    // 2-2. 대기 상태 redis 에서 지우기
    private void removeFromRedis(Long exerciseType, String userToken) {
        String queueKey = getQueueKey(exerciseType);
        String sortedSetKey = getSortedSetKey(exerciseType);
        String userInfoKey = getUserInfoKey(exerciseType);
        String expireKey = getUserJoinTimeKey(exerciseType, userToken);

//        Long removedFromQueue = redisTemplate.opsForList().remove(queueKey, 1, userToken);
        Long removedFromHash = redisTemplate.opsForHash().delete(userInfoKey, userToken);
        Long removedFromSortedSet = redisTemplate.opsForZSet().remove(sortedSetKey, userToken);
        redisTemplate.delete(expireKey);

//        log.info("[REDIS REMOVE] Queue에서 제거된 아이템 수: {}", removedFromQueue); // 사실 얘는 삭제 안됨
        log.info("[REDIS REMOVE] 해시에서 제거된 아이템 수: {}", removedFromHash);
        log.info("[REDIS REMOVE] 정렬 세트에서 제거된 아이템 수: {}", removedFromSortedSet);
    }

    // 매칭 처리 로직 (스케줄러로 주기적으로 실행할 것)
    @Scheduled(fixedRate = 5000) // 5초마다 실행
    public void deleteUsers() {
        List<Long> exerciseTypes = exerciseStatsRatioRepository.findAllExerciseStatsRatioId();

        for (Long id : exerciseTypes) {
            String queueKey = getQueueKey(id);
            String sortedSetKey = getSortedSetKey(id);
            String userInfoKey = getUserInfoKey(id);

            List<Object> waitingUsers = redisTemplate.opsForList().range(queueKey, 0, -1);
            if (waitingUsers == null || waitingUsers.isEmpty()) continue;

            long currentTime = System.currentTimeMillis();
            for (Object userToken : waitingUsers) {
                String expireKey = getUserJoinTimeKey(id, userToken.toString());

                Object expireTimeObj;
                try {
                    expireTimeObj = redisTemplate.opsForValue().get(expireKey);
                } catch (Exception e) {
                    log.error("[Matching] Redis 값 조회 중 오류 발생 (운동 타입: {}, 키: {}), 오류: {}", id, expireKey, e.getMessage());
                    continue;
                }

                if (expireTimeObj == null) continue;

                String expireTimeStr = expireTimeObj.toString();

                // 숫자인지 확인 후 변환 (EXPIRED 같은 값 방지)
                if (!expireTimeStr.matches("\\d+")) {
                    log.warn("[Matching] 예상치 못한 값 '{}' 발견, 해당 키 삭제 (운동 타입: {})", expireTimeStr, id);
                    redisTemplate.delete(expireKey); // 이상한 값이면 삭제
                    continue;
                }

                long joinTime = Long.parseLong(expireTimeStr);

                if (currentTime - joinTime >= 17000) { // 17초 초과 시 삭제
                    redisTemplate.opsForList().remove(queueKey, 1, userToken);
                    redisTemplate.opsForZSet().remove(sortedSetKey, userToken);
                    redisTemplate.opsForHash().delete(userInfoKey, userToken);
                    redisTemplate.delete(expireKey);

                    log.info("[Matching] 대기 시간이 초과된 사용자 {} 제거완료 (운동 타입: {})", userToken, id);
                } else {
                    log.info("[Matching] 대기 시간이 남아있는 사용자 {} 발견, 삭제 중단 (운동 타입: {})", userToken, id);
                    break; // 이후 사용자들은 아직 시간이 남아있으므로 더 이상 확인 X
                }
            }
        }
    }


    /*private void processMatchingForExercise(String exerciseType) {
        System.out.println("\n========== " + exerciseType + " 대기방 현재 상태 ==========");

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
        System.out.println("\n========== " + exerciseType + " 매칭 시도 ==========");
        log.info("=== {} 매칭 시도 ===", exerciseType);
        logWaitingRoomStatus(exerciseType);
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
    }*/

    /*private void createMatch(String exerciseType, String user1, String user2) {
        // 매칭 정보 생성 및 저장 로직
        String matchId = UUID.randomUUID().toString();
        // DB에 매칭 정보 저장

        // WebSocket으로 매칭 성공 알림
        notifyMatchSuccess(user1, user2, matchId);
    }*/
    

    /*private void notifyMatchSuccess(String user1, String user2, String matchId) {
        // 매칭 정보 생성
        MatchSuccessRes matchInfo = createMatchSuccessDTO(user1, user2, matchId);

        // 각 유저에게 WebSocket으로 매칭 성공 메시지 전송
        messagingTemplate.convertAndSend("/topic/match/" + user1, matchInfo);
        messagingTemplate.convertAndSend("/topic/match/" + user2, matchInfo);

        log.info("Match success notification sent to users {} and {}", user1, user2);
    }*/

    /*private MatchSuccessRes createMatchSuccessDTO(String user1, String user2, String matchId) {
        return MatchSuccessRes.builder()
                .matchId(matchId)
                .player1Id(user1)
                .player2Id(user2)
                .matchedAt(LocalDateTime.now())
                .build();
    }*/

}
