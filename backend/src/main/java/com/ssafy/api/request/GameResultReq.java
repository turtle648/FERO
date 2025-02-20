package com.ssafy.api.request;

import lombok.*;

@Getter
@RequiredArgsConstructor
@ToString
public class GameResultReq {
    private final Long exerciseId;
    private final String gameId;
    private final int duration;
    private final String userToken1;
    private final String userToken2;
    private final Integer user1Score;
    private final Integer user2Score;
    private final Integer remainTime;
}
