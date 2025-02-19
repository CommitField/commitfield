package cmf.commitField.global.globalDto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GlobalResponseCode {

    OK(200, "요청이 성공하였습니다."),
    BAD_REQUEST(400, "잘못된 요청입니다."),
    NOT_FOUND(404, "찾을 수 없습니다."),
    INTERNAL_SERVER_ERROR(500, "서버 내부 오류가 발생하였습니다.");

    private final int code;        // HTTP 상태 코드
    private final String message;  // 응답 메시지
}
