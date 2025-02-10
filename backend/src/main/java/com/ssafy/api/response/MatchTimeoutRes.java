package com.ssafy.api.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class MatchTimeoutRes {
    private String userId;
    private String message;
    private LocalDateTime timeoutAt;
}
