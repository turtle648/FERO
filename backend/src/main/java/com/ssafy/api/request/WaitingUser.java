package com.ssafy.api.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.socket.WebSocketSession;

import java.time.Duration;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class WaitingUser {
    private String userId;
    private String exerciseType;
    private int rankScore;
    private LocalDateTime joinTime;

    @JsonIgnore
    public boolean isExpired() {
        return Duration.between(joinTime, LocalDateTime.now()).toMinutes() >= 5;
    }
}
