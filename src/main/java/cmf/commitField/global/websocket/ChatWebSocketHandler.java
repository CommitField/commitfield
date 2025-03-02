package cmf.commitField.global.websocket;

import cmf.commitField.global.error.ErrorCode;
import cmf.commitField.global.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.*;

@Component
@Slf4j
public class ChatWebSocketHandler implements WebSocketHandler {

    private final Map<Long, List<WebSocketSession>> chatRooms = new HashMap<>();
    // 방의 키값


    // 연결이 되었을 때
    @Override
    public void afterConnectionEstablished(WebSocketSession session)
            throws Exception {
//        list.add(session);
        Long roomId = extractRoomId(session);
        // roomId 가 없을 경우, session list (new ArrayList)
        List<WebSocketSession> roomSessions = chatRooms.getOrDefault(roomId, new ArrayList<>());
        // 세션 추가
        roomSessions.add(session);
        // 해당 방의 키값에 session list 추가
        chatRooms.put(roomId, roomSessions);
        log.info(session + "의 클라이언트 접속");
    }

    // 클라이언트로부터 받은 메시지를 처리하는 로직
    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message)
            throws Exception {
        // 메시지 처리 로직
        Long roomId = extractRoomId(session);
        List<WebSocketSession> roomSessions = chatRooms.get(roomId);
        if (roomSessions != null) {
            String payload = message.getPayload().toString();
            log.info("전송 메시지: " + payload);

            for (WebSocketSession msg : roomSessions) {
                try {
                    msg.sendMessage(message);
                } catch (IOException e) {
                    throw new CustomException(ErrorCode.CHAT_ERROR);
                }
            }
        } else {
            log.info("해당 채팅방에 클라이언트가 없습니다.");
            throw new CustomException(ErrorCode.NOT_EXIST_CLIENT);
        }
    }

    //오류 처리 로직을 구현 (네트워크 오류, 프로토콜 오류, 처리 오류... 생각 중)
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception)
            throws Exception {
        log.error(exception.getMessage());
    }

    // 연결 종료되었을 때
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus)
            throws Exception {
        Long roomId = extractRoomId(session); // 클라이언트가 속한 채팅방 ID를 추출

        List<WebSocketSession> roomSessions = chatRooms.get(roomId);
        if (roomSessions != null) {
            roomSessions.remove(session);
        }
        log.info(session + "의 클라이언트 접속 해제");
    }

    //부분 메시지를 지원하는지 여부를 반환 (아직까지는 필요 없으니 false)
    //대용량(사진이나 동영상 등)이 필요한 경우에는 따로 구현할 필요가 있음.
    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    private Long extractRoomId(WebSocketSession session) {
        Long roomId = null;
        String uri = Objects.requireNonNull(session.getUri()).toString();
        String[] uriParts = uri.split("/");
        // EX_URL) /chat/room/{roomId} 일 때 roomId 추출
        // 늘어난다면 수 변경해주면.. (일단 임시로 설정)
//        if (uriParts.length >= 3 && uriParts[2].equals("room")) {
//            roomId = Long.valueOf(uriParts[3]);
        if (uriParts.length >= 4 && uriParts[2].equals("msg")) {
            return Long.valueOf(uriParts[3]);
        }
        // /chat/room/join/{roomId}, /chat/room/out/{roomId}, /chat/room/delete/{roomId} 일 때 roomId 추출
        if (uriParts.length >= 5 && uriParts[2].equals("room") &&
                (uriParts[3].equals("join") || uriParts[3].equals("out") || uriParts[3].equals("delete"))) {
            roomId = Long.valueOf(uriParts[4]);
        }
        return roomId;
    }
}