package cmf.commitField.global.websocket;

import cmf.commitField.domain.chat.chatMessage.controller.request.ChatMsgRequest;
import cmf.commitField.domain.chat.chatMessage.controller.response.ChatMsgResponse;
import cmf.commitField.domain.chat.chatMessage.service.ChatMessageService;
import cmf.commitField.domain.user.entity.User;
import cmf.commitField.domain.user.repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class ChatWebSocketHandler implements WebSocketHandler {

    private final Map<Long, List<WebSocketSession>> chatRooms = new HashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ChatMessageService chatMessageService;
    private final UserRepository userRepository;

    // 연결이 되었을 때
    @Override
    public void afterConnectionEstablished(WebSocketSession session)
            throws Exception {
        log.info("클라이언트 접속: {}", session.getId());

        // 연결 성공 메시지 전송
        Map<String, Object> connectMessage = new HashMap<>();
        connectMessage.put("type", "SYSTEM");
        connectMessage.put("message", "채팅 서버에 연결되었습니다.");
        connectMessage.put("timestamp", LocalDateTime.now().toString());

        try {
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(connectMessage)));
        } catch (Exception e) {
            log.error("연결 메시지 전송 실패: {}", e.getMessage());
        }
    }

    // 클라이언트로부터 받은 메시지를 처리하는 로직
    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message)
            throws Exception {
        String payload = message.getPayload().toString();
        log.info("메시지 수신: {}", payload);

        try {
            JsonNode jsonNode = objectMapper.readTree(payload);
            String messageType = jsonNode.has("type") ? jsonNode.get("type").asText() : "UNKNOWN";

            switch (messageType) {
                case "SUBSCRIBE":
                    handleSubscribe(session, jsonNode);
                    break;
                case "UNSUBSCRIBE":
                    handleUnsubscribe(session, jsonNode);
                    break;
                case "CHAT":
                    handleChatMessage(session, jsonNode);
                    break;
                default:
                    log.warn("알 수 없는 메시지 타입: {}", messageType);
                    sendErrorMessage(session, "지원하지 않는 메시지 타입입니다: " + messageType);
            }
        } catch (Exception e) {
            log.error("메시지 처리 중 오류 발생: {}", e.getMessage(), e);
            // 오류 메시지 전송
            sendErrorMessage(session, "메시지 처리 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    // 구독 메시지 처리
    private void handleSubscribe(WebSocketSession session, JsonNode jsonNode) {
        try {
            if (!jsonNode.has("roomId")) {
                sendErrorMessage(session, "roomId 필드가 누락되었습니다.");
                return;
            }

            Long roomId = jsonNode.get("roomId").asLong();
            log.info("채팅방 구독 요청: roomId={}, sessionId={}", roomId, session.getId());

            // 해당 룸의 세션 목록에 추가
            List<WebSocketSession> roomSessions = chatRooms.getOrDefault(roomId, new ArrayList<>());

            // 이미 등록된 세션인지 확인하여 중복 등록 방지
            boolean alreadyRegistered = roomSessions.stream()
                    .anyMatch(existingSession -> existingSession.getId().equals(session.getId()));

            if (!alreadyRegistered) {
                roomSessions.add(session);
                chatRooms.put(roomId, roomSessions);
                log.info("클라이언트 세션 {}가 룸 {}에 구독됨", session.getId(), roomId);

                // 구독 확인 메시지 전송
                Map<String, Object> subscribeResponse = new HashMap<>();
                subscribeResponse.put("type", "SUBSCRIBE_ACK");
                subscribeResponse.put("roomId", roomId);
                subscribeResponse.put("timestamp", LocalDateTime.now().toString());
                subscribeResponse.put("message", "채팅방에 연결되었습니다.");

                session.sendMessage(new TextMessage(objectMapper.writeValueAsString(subscribeResponse)));
            } else {
                log.info("이미 구독 중인 세션: sessionId={}, roomId={}", session.getId(), roomId);
            }
        } catch (Exception e) {
            log.error("구독 처리 중 오류: {}", e.getMessage(), e);
            try {
                sendErrorMessage(session, "구독 처리 중 오류: " + e.getMessage());
            } catch (IOException ex) {
                log.error("오류 메시지 전송 실패: {}", ex.getMessage());
            }
        }
    }

    // 구독 해제 메시지 처리
    private void handleUnsubscribe(WebSocketSession session, JsonNode jsonNode) {
        try {
            if (!jsonNode.has("roomId")) {
                sendErrorMessage(session, "roomId 필드가 누락되었습니다.");
                return;
            }

            Long roomId = jsonNode.get("roomId").asLong();

            List<WebSocketSession> roomSessions = chatRooms.get(roomId);
            if (roomSessions != null) {
                roomSessions.removeIf(existingSession -> existingSession.getId().equals(session.getId()));
                log.info("클라이언트 세션 {}가 룸 {}에서 구독 해제됨", session.getId(), roomId);

                // 구독 해제가 성공적으로 처리되었음을 알리는 메시지 전송
                Map<String, Object> unsubscribeResponse = new HashMap<>();
                unsubscribeResponse.put("type", "UNSUBSCRIBE_ACK");
                unsubscribeResponse.put("roomId", roomId);
                unsubscribeResponse.put("timestamp", LocalDateTime.now().toString());
                unsubscribeResponse.put("message", "채팅방에서 연결이 해제되었습니다.");

                session.sendMessage(new TextMessage(objectMapper.writeValueAsString(unsubscribeResponse)));
            } else {
                log.warn("존재하지 않는 채팅방 구독 해제 시도: roomId={}", roomId);
                sendErrorMessage(session, "존재하지 않는 채팅방입니다: " + roomId);
            }
        } catch (Exception e) {
            log.error("구독 해제 처리 중 오류: {}", e.getMessage(), e);
            try {
                sendErrorMessage(session, "구독 해제 처리 중 오류: " + e.getMessage());
            } catch (IOException ex) {
                log.error("오류 메시지 전송 실패: {}", ex.getMessage());
            }
        }
    }

    // 채팅 메시지 처리
    private void handleChatMessage(WebSocketSession session, JsonNode jsonNode) {
        try {
            // 필수 필드 검증
            if (!jsonNode.has("roomId") || !jsonNode.has("message") || !jsonNode.has("userId")) {
                sendErrorMessage(session, "필수 필드가 누락되었습니다. (roomId, message, userId 필요)");
                return;
            }

            Long roomId = jsonNode.get("roomId").asLong();
            Long userId = jsonNode.get("userId").asLong();
            String message = jsonNode.get("message").asText();
            String from = jsonNode.has("from") ? jsonNode.get("from").asText() : null;

            log.info("채팅 메시지: roomId={}, userId={}, message={}, from={}", roomId, userId, message, from);

            if (message == null || message.trim().isEmpty()) {
                sendErrorMessage(session, "메시지 내용이 비어있습니다.");
                return;
            }

            // 사용자 정보 검증
            User user = userRepository.findById(userId).orElse(null);
            if (user == null) {
                log.warn("존재하지 않는 사용자: userId={}", userId);
                sendErrorMessage(session, "존재하지 않는 사용자입니다.");
                return;
            }

            // 메시지 저장 및 처리
            try {
                ChatMsgRequest chatMsgRequest = new ChatMsgRequest(message);
                ChatMsgResponse response = chatMessageService.sendMessage(chatMsgRequest, userId, roomId);

                // 웹소켓 메시지 포맷 생성 (클라이언트와 일치시킴)
                Map<String, Object> wsMessage = new HashMap<>();
                wsMessage.put("type", "CHAT");
                wsMessage.put("roomId", roomId);
                wsMessage.put("userId", userId);
                wsMessage.put("chatMsgId", response.getChatMsgId()); // DB에 저장된 메시지 ID 추가
                wsMessage.put("from", response.getFrom());
                wsMessage.put("nickname", response.getFrom()); // 클라이언트 호환성을 위해 두 필드 모두 설정
                wsMessage.put("message", message);
                wsMessage.put("sendAt", response.getSendAt().toString());

                // 메시지 JSON 변환
                String messageJson = objectMapper.writeValueAsString(wsMessage);
                log.info("Broadcasting message: {}", messageJson);

                // 해당 채팅방의 모든 세션에 메시지 브로드캐스트
                broadcastMessageToRoom(roomId, messageJson);
            } catch (Exception e) {
                log.error("메시지 저장 처리 중 오류: {}", e.getMessage(), e);
                sendErrorMessage(session, "메시지 전송 중 오류가 발생했습니다: " + e.getMessage());
            }

        } catch (Exception e) {
            log.error("채팅 메시지 처리 중 오류: {}", e.getMessage(), e);
            try {
                sendErrorMessage(session, "메시지 전송 중 오류가 발생했습니다: " + e.getMessage());
            } catch (IOException ex) {
                log.error("오류 메시지 전송 실패: {}", ex.getMessage());
            }
        }
    }

    // 특정 채팅방에 메시지 브로드캐스트
    private void broadcastMessageToRoom(Long roomId, String message) {
        List<WebSocketSession> roomSessions = chatRooms.get(roomId);
        if (roomSessions != null) {
            List<WebSocketSession> failedSessions = new ArrayList<>();

            for (WebSocketSession session : roomSessions) {
                try {
                    if (session.isOpen()) {
                        log.debug("Broadcasting to session {}", session.getId());
                        session.sendMessage(new TextMessage(message));
                    } else {
                        failedSessions.add(session);
                        log.debug("Session closed, adding to failed sessions: {}", session.getId());
                    }
                } catch (IOException e) {
                    log.error("메시지 브로드캐스트 중 오류: {}", e.getMessage());
                    failedSessions.add(session);
                }
            }

            // 실패한 세션 정리
            if (!failedSessions.isEmpty()) {
                log.info("닫힌 세션 정리: {} 개의 세션 제거", failedSessions.size());
                roomSessions.removeAll(failedSessions);
            }
        } else {
            log.warn("존재하지 않는 채팅방에 메시지 전송 시도: roomId={}", roomId);
        }
    }

    // 오류 메시지 전송
    private void sendErrorMessage(WebSocketSession session, String errorMessage) throws IOException {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("type", "ERROR");
        errorResponse.put("message", errorMessage);
        errorResponse.put("timestamp", LocalDateTime.now().toString());

        session.sendMessage(new TextMessage(objectMapper.writeValueAsString(errorResponse)));
    }

    // 오류 처리 로직
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception)
            throws Exception {
        log.error("WebSocket 통신 오류: {}", exception.getMessage(), exception);
    }

    // 연결 종료되었을 때
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus)
            throws Exception {
        log.info("클라이언트 접속 해제: {}, 상태코드: {}", session.getId(), closeStatus);

        // 모든 채팅방에서 세션 제거
        for (Map.Entry<Long, List<WebSocketSession>> entry : chatRooms.entrySet()) {
            Long roomId = entry.getKey();
            List<WebSocketSession> roomSessions = entry.getValue();

            boolean removed = roomSessions.removeIf(existingSession ->
                    existingSession.getId().equals(session.getId()));

            if (removed) {
                log.info("세션이 채팅방 {}에서 제거됨: {}", roomId, session.getId());
            }
        }
    }

    // 부분 메시지 지원 여부
    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}