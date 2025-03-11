package cmf.commitField.global.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@RequiredArgsConstructor
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final ChatWebSocketHandler chatWebSocketHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // 채팅 웹소켓 핸들러 등록
        registry.addHandler(chatWebSocketHandler, "/chat-rooms")
                .setAllowedOrigins("*"); // CORS 설정, 실제 환경에서는 보안을 위해 제한적으로 설정해야 함
    }
}