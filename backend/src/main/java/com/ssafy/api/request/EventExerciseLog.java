package com.ssafy.api.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@ToString
public class EventExerciseLog {
    private final String userId;
    private final ExerciseLogReq req;
}
