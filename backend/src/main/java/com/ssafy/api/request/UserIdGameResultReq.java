package com.ssafy.api.request;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@RequiredArgsConstructor
public class UserIdGameResultReq {
    private final Long exerciseId;
    private final String gameId;
    private final int duration;
    private final String user1Id;
    private final String user2Id;
    private final Integer user1Score;
    private final Integer user2Score;
}
