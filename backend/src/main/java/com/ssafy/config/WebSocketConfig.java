package com.ssafy.config;

import com.ssafy.api.handler.ChatWebSocketHandler;
import com.ssafy.api.handler.SignalingHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer, WebSocketMessageBrokerConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(chatHandler(), "/api/v1/chatrooms/{roomId}/ws")
                .addHandler(signalingSocketHandler(), "/api/v1/videorooms")
                .addHandler(matchingHandler(), "/api/v1/matching")  // 매칭용 핸들러 추가
                .addInterceptors(new HttpSessionHandshakeInterceptor())
                .setAllowedOrigins("*");
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");  // 구독 prefix
        config.setApplicationDestinationPrefixes("/app");   // 메시지 발행 prefix
    }

    @Bean
    public WebSocketHandler signalingSocketHandler() {
        return new SignalingHandler();
    }

    @Bean
    public WebSocketHandler chatHandler() {
        return new ChatWebSocketHandler();
    }

    @Bean
    public WebSocketHandler matchingHandler() {
        return new MatchingWebSocketHandler();  // 새로운 매칭 핸들러
    }
}
