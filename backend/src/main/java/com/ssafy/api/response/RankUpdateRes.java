package com.ssafy.api.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RankUpdateRes {
    private String winnerId;
    private short winnerPreviousScore;
    private short winnerNewScore;
    private String loserId;
    private short loserPreviousScore;
    private short loserNewScore;
}