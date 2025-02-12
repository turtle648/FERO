package com.ssafy.api.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public class ExerciseResultEvent {
    private final String userToken1;
    private final String userToken2;
    private final int userScore1;
    private final int userScore2;
    private final int result;
    private final Long exerciseType;
}
