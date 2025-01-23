package com.ssafy.common.redis.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

@Service
@Slf4j
public class RedisService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final Duration DEFAULT_TIMEOUT = Duration.ofMinutes(30);

    public RedisService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void saveSessionData(String sessionId, Object data) {
        redisTemplate.opsForValue().set(sessionId, data, DEFAULT_TIMEOUT);
        log.debug("Saved session data for ID: {}", sessionId);
    }

    public Optional<Object> getSessionData(String sessionId) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(sessionId));
    }
}