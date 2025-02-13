package com.ssafy.api.request;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@RequiredArgsConstructor
public class GameResultReq {
    private final Long exerciseId;
    private final String gameId;
    private final int duration;
    private final String userToken1;
    private final String userToken2;
    private final Integer user1Score;
    private final Integer user2Score;
}
