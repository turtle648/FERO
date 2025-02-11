package com.ssafy.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.api.request.EnterWaitingRoomEvent;
import com.ssafy.api.request.WaitingUser;
import com.ssafy.db.repository.ExerciseStatsRatioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
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
    private final ApplicationEventPublisher eventPublisher;

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

        // 사용자 입장 이벤트 발생
        eventPublisher.publishEvent(new EnterWaitingRoomEvent(userToken, exerciseType, rankScore));
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


    // 3-1. 매칭 시도 이벤트 리스너
    @EventListener
    public void processMatching(EnterWaitingRoomEvent event){
        log.info("🎯 매칭 프로세스 시작 - User: {}, Exercise: {}, Score: {}",
                event.getUserToken(), event.getExerciseId(), event.getRankScore());

        processMatchingLogic(event.getExerciseId());
    }

    // 3-2. 실제 매칭 시도
    private void processMatchingLogic(Long exerciseId) {
        String queueKey = getQueueKey(exerciseId);
        String sortedSetKey = getSortedSetKey(exerciseId);

        // 1. queue 확인 - 첫 번째 유저부터 순차적으로 확인
        List<Object> queue = redisTemplate.opsForList().range(queueKey, 0, -1);
        if (queue == null || queue.isEmpty()) {
            log.info("Queue is empty for exercise type: {}", exerciseId);
            return;
        }

        // 2. queue 순회하면서 매칭 시도하기
        int queueSize = queue.size();
        int currentIndex = 0;

        while(currentIndex < queueSize) {
            String currentUserToken = queue.get(currentIndex).toString();

            // 3. sortedSet에서 현재 유저의 점수 확인
            Double currentUserScore = redisTemplate.opsForZSet().score(sortedSetKey, currentUserToken);

            // 3-1. sortedSet에 없는 경우 (이미 매칭되어서 나가거나 타임아웃)
            if (currentUserScore == null) {
                log.info("User {} not found in sorted set, removing from queue", currentUserToken);
                redisTemplate.opsForList().remove(queueKey, 1, currentUserToken);
                currentIndex++;
                continue;
            }
            boolean matchFound = false;
            // 3-2. 매칭 가능한 상대 찾기 (rankScore +- 100 범위)
            for (int i=currentIndex+1; i < queueSize; i++) {
                String candidateUserToken = queue.get(i).toString();
                Double candidateScore = redisTemplate.opsForZSet().score(sortedSetKey, candidateUserToken);

                // 후보자가 sorted-set 에 없으면 스킵 -- queue에서 삭제
                if (candidateScore == null) {
                    redisTemplate.opsForList().remove(queueKey, 1, candidateUserToken);
                    continue;
                }

                // 점수 차이가 100 이내인지 확인
                System.out.println("들어온 애 점수 : "+currentUserScore);
                System.out.println("후보자 점수 : "+candidateScore);
                if (Math.abs(currentUserScore - candidateScore) <= 100) {
                    // 매칭 성공
                    handleMatchSuccess(currentUserToken, candidateUserToken, exerciseId);
                    matchFound = true;
                    return; // 매칭 완료되면 종료
                }
            }
            if (!matchFound) {
                log.info("적합한 매칭 상대를 찾지 못함 (score: {}), moving to next user",
                        currentUserScore);
            }
            currentIndex++;
        }
    }

    private void handleMatchSuccess(String userToken1, String userToken2, Long exerciseId) {
        removeFromRedis(exerciseId, userToken1);
        removeFromRedis(exerciseId, userToken2);

        log.info("🎊 매칭 성공! User1: {}, User2: {}, Exercise: {}", userToken1, userToken2, exerciseId);
    }

}
