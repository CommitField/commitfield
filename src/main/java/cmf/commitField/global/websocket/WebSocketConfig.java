package cmf.commitField.global.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 클라이언트가 연결할 WebSocket 엔드포인트
        registry.addEndpoint("/ws-stomp")
                .setAllowedOriginPatterns("*") // setAllowedOriginPatterns 사용
                // SockJS 사용 (WebSocket 미지원 브라우저 대응)
                .withSockJS();

        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*") // setAllowedOriginPatterns 사용
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 메시지 송신 prefix
        registry.setApplicationDestinationPrefixes("/app");
        // 메시지 수신 prefix
        registry.enableSimpleBroker("/sub", "/topic", "/queue");
    }
}
