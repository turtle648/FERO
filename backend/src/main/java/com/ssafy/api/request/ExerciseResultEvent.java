package com.ssafy.api.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public class ExerciseResultEvent {
    private final String user1Id;
    private final String user2Id;
    private final int userScore1;
    private final int userScore2;
    private final int result;
    private final Long exerciseType;
}
