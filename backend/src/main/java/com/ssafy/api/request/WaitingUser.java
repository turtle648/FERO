package com.ssafy.api.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.socket.WebSocketSession;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WaitingUser implements Serializable {
    private static final long serialVersionUID = 1L;

    private String userId;
    private String exerciseType;
    private Short rankScore;
    private LocalDateTime joinTime;

    @JsonIgnore
    public boolean isExpired() {
        return Duration.between(joinTime, LocalDateTime.now()).toMinutes() >= 1; // 타임아웃 체크 5분
    }
}
