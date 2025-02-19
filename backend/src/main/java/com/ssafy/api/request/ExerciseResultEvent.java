package com.ssafy.api.request;

import lombok.*;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseResultEvent {
    private String user1Id;
    private String user2Id;
    private int userScore1;
    private int userScore2;
    private int result;
    private Long exerciseType;
}
