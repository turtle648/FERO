package com.ssafy.config;

import com.ssafy.api.handler.ChatWebSocketHandler;
import com.ssafy.api.handler.SignalingHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // 웹소켓 엔드포인트를 /api/v1/chatrooms/{roomId}/ws 로 설정
        registry.addHandler(chatHandler(), "/api/v1/chatrooms/{roomId}/ws")
                .addHandler(signalingSocketHandler(), "/api/v1/videorooms")
                .addInterceptors(new HttpSessionHandshakeInterceptor()) // 세션 정보 전달
                .setAllowedOrigins("*");  // 모든 출처에서 접근 허용
    }

    @Bean
    public WebSocketHandler signalingSocketHandler() {
        return new SignalingHandler();
    }

    @Bean
    public WebSocketHandler chatHandler() {
        return new ChatWebSocketHandler();  // 웹소켓 메시지를 처리할 핸들러
    }
}
