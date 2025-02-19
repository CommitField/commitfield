package cmf.commitField.global.error;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

@Getter
public class ErrorResponse {
    private final String timestamp;
    private final String error;
    private final String message;

    @Builder
    public ErrorResponse(String timestamp, String error, String message){
        this.timestamp = timestamp;
        this.error = error;
        this.message = message;
    }
    public static ResponseEntity<ErrorResponse> toResponseEntity(ErrorCode errorCode){
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ErrorResponse.builder()
                        .timestamp(LocalDateTime.now().toString())
                        .error(errorCode.getHttpStatus().name())
                        .message(errorCode.getMessage())
                        .build()
                );

    }
}