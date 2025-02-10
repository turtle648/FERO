package com.ssafy.api.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class MatchSuccessRes {
    private String matchId;
    private String player1Id;
    private String player2Id;
    private LocalDateTime matchedAt;
}
