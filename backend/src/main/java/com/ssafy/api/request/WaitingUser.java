package com.ssafy.api.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.socket.WebSocketSession;

@Getter
@Setter
@AllArgsConstructor
public class WaitingUser {
    private String userId;
    private WebSocketSession session;
    private int rankScore;
}
