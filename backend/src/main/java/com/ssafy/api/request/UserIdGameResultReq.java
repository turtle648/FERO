package com.ssafy.api.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserIdGameResultReq {
    private Long exerciseId;
    private String gameId;
    private int duration;
    private String user1Id;
    private String user2Id;
    private Integer user1Score;
    private Integer user2Score;
}
