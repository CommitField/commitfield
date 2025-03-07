package cmf.commitField.global.globalDto;

import cmf.commitField.global.error.ErrorCode;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class GlobalResponse<T> {

    private final LocalDateTime timestamp; // 응답 생성 시간
    private final int statusCode;          // HTTP 상태 코드
    private final String message;          // 응답 메시지
    private final T data;                  // 응답 데이터 (성공 시 데이터, 실패 시 추가 정보)

    // 성공 응답 생성자
    private GlobalResponse(GlobalResponseCode responseCode, T data) {
        this.timestamp = LocalDateTime.now();
        this.statusCode = responseCode.getCode();
        this.message = responseCode.getMessage();
        this.data = data;
    }

    // 에러 응답 생성자
    private GlobalResponse(ErrorCode errorCode, T data) {
        this.timestamp = LocalDateTime.now();
        this.statusCode = errorCode.getHttpStatus().value();
        this.message = errorCode.getMessage();
        this.data = data;
    }

    // 메시지와 데이터를 받는 생성자
    private GlobalResponse(int statusCode, String message, T data) {
        this.timestamp = LocalDateTime.now();
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
    }

    // 성공 응답 (데이터 포함)
    public static <T> GlobalResponse<T> success(String message, T data) {
        return new GlobalResponse<>(GlobalResponseCode.OK.getCode(), message, data);
    }

    // 성공 응답 (데이터 포함)
    public static <T> GlobalResponse<T> success(T data) {
        return new GlobalResponse<>(GlobalResponseCode.OK, data);
    }

    // 성공 응답 (데이터 없음)
    public static <T> GlobalResponse<T> success(String message) {
        return success(message, null);
    }

    // 에러 응답 (데이터 포함)
    public static <T> GlobalResponse<T> error(ErrorCode errorCode, T data) {
        return new GlobalResponse<>(errorCode, data);
    }

    // 성공 응답 (기본 메세지)
    public static <T> GlobalResponse<T> success() {
        return success(null);
    }

    // 에러 응답 (데이터 없음)
    public static <T> GlobalResponse<T> error(ErrorCode errorCode) {
        return error(errorCode, null);
    }
}
