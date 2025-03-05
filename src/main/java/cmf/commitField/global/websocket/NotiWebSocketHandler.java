package cmf.commitField.global.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class NotiWebSocketHandler implements WebSocketHandler {

    private final List<WebSocketSession> sessions = new ArrayList<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        log.info("알림 WebSocket 연결됨: " + session);
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        // 알림 메시지 처리 로직 (필요 시 구현)
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.error("알림 WebSocket 오류: " + exception.getMessage());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        sessions.remove(session);
        log.info("알림 WebSocket 연결 종료됨: " + session);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    // 모든 유저에게 알림 메시지 전송
    public void sendNotificationToAllUsers(String message) {
        for (WebSocketSession session : sessions) {
            try {
                session.sendMessage(new TextMessage(message));
            } catch (IOException e) {
                log.error("알림 메시지 전송 실패: " + e.getMessage());
            }
        }
    }
}