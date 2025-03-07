package cmf.commitField.global.websocket;

import cmf.commitField.domain.noti.noti.dto.NotiDto;
import cmf.commitField.domain.noti.noti.entity.Noti;
import cmf.commitField.domain.noti.noti.service.NotiService;
import cmf.commitField.domain.user.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotiWebSocketHandler implements WebSocketHandler {
    private final NotiService notiService;
    private final ObjectMapper objectMapper;
    private final Map<Long, WebSocketSession> sessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("클라이언트 접속: {}", session.getId());

        // 연결 성공 메시지 전송
        Map<String, Object> connectMessage = new HashMap<>();
        connectMessage.put("type", "SYSTEM");
        connectMessage.put("connect", "알림 서버에 연결되었습니다.");
        connectMessage.put("timestamp", LocalDateTime.now().toString());

        try {
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(connectMessage)));
        } catch (Exception e) {
            log.error("연결 메시지 전송 실패: {}", e.getMessage());
        }
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        if (message instanceof TextMessage) {
            String payload = ((TextMessage) message).getPayload();
            log.info("Received message: {}", payload);
        } else {
            log.warn("Received unsupported message type: {}", message.getClass().getSimpleName());
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.error("WebSocket error: ", exception);
        session.close(CloseStatus.SERVER_ERROR);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.values().remove(session);
        log.info("WebSocket disconnected: {}", status);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    public void sendNotification(User receiver, List<NotiDto> noti) {
        WebSocketSession session = sessions.get(receiver.getId());
        if (session != null && session.isOpen()) {
            try {
                String payload = objectMapper.writeValueAsString(noti);
                session.sendMessage(new TextMessage(payload));
            } catch (IOException e) {
                log.error("Failed to send WebSocket notification", e);
            }
        }
    }
}