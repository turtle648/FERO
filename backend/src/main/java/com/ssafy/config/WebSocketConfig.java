package com.ssafy.config;

import com.ssafy.api.handler.ChatWebSocketHandler;
import com.ssafy.api.handler.MatchingWebSocketHandler;
import com.ssafy.api.handler.SignalingHandler;
import com.ssafy.api.service.MatchingService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

@Configuration
@EnableWebSocket
@EnableScheduling
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {
    private final ApplicationEventPublisher eventPublisher;
    private final MatchingService matchingService;
    private final ApplicationContext eventHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(chatHandler(), "/api/v1/chatrooms/{roomId}/ws")
                .addHandler(signalingSocketHandler(), "/api/v1/videorooms")
                .addHandler(matchingHandler(), "/api/v1/matching")
                .addInterceptors(new HttpSessionHandshakeInterceptor())
                .setAllowedOrigins("*"); // 특정 오리진 명시
    }

    @Bean
    public WebSocketHandler signalingSocketHandler() {
        return new SignalingHandler(eventPublisher, matchingService, eventHandler);
    }

    @Bean
    public WebSocketHandler chatHandler() {
        return new ChatWebSocketHandler();
    }

    @Bean
    public WebSocketHandler matchingHandler() {
        return new MatchingWebSocketHandler(matchingService);
    }
}
