package cmf.commitField.global.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // Common
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "제공된 입력 값이 유효하지 않습니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "허용되지 않은 요청 방식입니다."),
    ENTITY_NOT_FOUND(HttpStatus.BAD_REQUEST, "요청한 엔티티를 찾을 수 없습니다."),
    INVALID_TYPE_VALUE(HttpStatus.BAD_REQUEST, "제공된 값의 타입이 유효하지 않습니다."),
    ERROR_PARSING_JSON_RESPONSE(HttpStatus.BAD_REQUEST, "JSON 응답을 파싱하는 중 오류가 발생했습니다."),
    MISSING_INPUT_VALUE(HttpStatus.BAD_REQUEST, "필수 입력 값이 누락되었습니다."),
    DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "데이터베이스 오류가 발생했습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다."),

    // User
    NOT_FOUND_USER(HttpStatus.BAD_REQUEST, "해당 유저가 존재하지 않습니다"),
    PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    NOT_AUTHENTICATED(HttpStatus.UNAUTHORIZED, "사용자가 인증되지 않았습니다."),

    // Auth
    LOGIN_REQUIRED(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다."),
    ACCESS_DENIED(HttpStatus.UNAUTHORIZED, "인증되지 않은 유저입니다."),
    SC_FORBIDDEN(HttpStatus.UNAUTHORIZED, "권한이 없는 유저입니다."),
    INVALID_JWT_SIGNATURE(HttpStatus.UNAUTHORIZED, "서명 검증에 실패했습니다." ),
    ILLEGAL_REGISTRATION_ID(HttpStatus.BAD_REQUEST,"해당 사항이 없는 로그인 경로입니다."),

    TOKEN_EXPIRED(HttpStatus.BAD_REQUEST, "토큰이 만료되었습니다."),

    // member
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다"),

    // season
    NOT_FOUND_SEASON(HttpStatus.NOT_FOUND, "시즌을 찾을 수 없습니다."),
    NOT_FOUND_SEASON_RANK(HttpStatus.NOT_FOUND, "해당 시즌의 랭킹을 찾을 수 없습니다."),

    //chatroom
    NOT_FOUND_ROOM(HttpStatus.NOT_FOUND, "이미 삭제된 방이거나 방을 찾을 수 없습니다."),
    ROOM_USER_FULL(HttpStatus.BAD_REQUEST, "방에 사용자가 다 차 있습니다."),
    CHAT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "채팅 전송에 오류가 있습니다."),
    NO_ROOM_FOUND(HttpStatus.NOT_FOUND, "채팅방이 없습니다."),
    NO_ROOM(HttpStatus.NOT_FOUND, "존재하지 않는 채팅방입니다."),

    //chatroom_title
    REQUEST_SAME_AS_CURRENT_TITLE(HttpStatus.BAD_REQUEST, "현재 제목과 바꾸려는 제목이 같습니다."),

    //user_chatroom
    NONE_ROOM(HttpStatus.NOT_FOUND, "사용자가 들어가 있는 방이 없습니다."),
    NOT_ROOM_CREATOR(HttpStatus.FORBIDDEN, "방 생성자가 아닙니다."),
    USER_CREATED_ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자가 생성한 방이 없습니다."),
    ALREADY_JOIN_ROOM(HttpStatus.BAD_REQUEST, "사용자는 이미 해당 방에 참여하고 있습니다."),
    NOT_EXIST_CLIENT(HttpStatus.NOT_FOUND, "채팅방에 사용자가 존재하지 않습니다."),
    NOT_ROOM_MEMBER(HttpStatus.FORBIDDEN, "채팅방에 속한 유저가 아닙니다."),

    //chatMessage
    EMPTY_MESSAGE(HttpStatus.BAD_REQUEST, "채팅 메시지는 공백으로 보낼 수 없습니다."),
    CHAT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 채팅방의 메시지들을 찾지 못했습니다."),

    // Lock
    FAILED_GET_LOCK(HttpStatus.LOCKED, "락을 획득하지 못했습니다."),

    // Check
    ERROR_CHECK(HttpStatus.BAD_REQUEST, "에러 체크"),

    //Heart
    NOT_EXIST_ROOM_HEART(HttpStatus.BAD_REQUEST, "해당 채팅방에 좋아요가 눌러져 있지 않습니다."),
    ALREADY_HEART_TO_ROOM(HttpStatus.BAD_REQUEST, "이미 해당 채팅방에 좋아요를 누르셨습니다."),
    NOT_FOUND_HEART(HttpStatus.NOT_FOUND, "좋아요 누른 채팅방이 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;
    }
