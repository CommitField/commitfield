package cmf.commitField.domain.noti.noti.dto;

import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@Getter
public class NotiDto {
    private Long id;
    private String message;
    private String formattedCreatedAt; // 변환된 날짜를 저장할 필드

    // 필드 값 직접 받는 생성자 추가
    public NotiDto(Long id, String message, LocalDateTime createdAt) {
        this.id = id;
        this.message = message;
        this.formattedCreatedAt = formatCreatedAt(createdAt); // 변환된 날짜 저장
    }

    // 날짜 변환 메서드 (오늘, 어제, 3일 전까지 이후 yyyy-MM-dd)
    private String formatCreatedAt(LocalDateTime createdAt) {
        LocalDateTime today = LocalDateTime.now();
        long daysBetween = ChronoUnit.DAYS.between(createdAt, today);

        if (daysBetween == 0) {
            return "오늘";
        } else if (daysBetween == 1) {
            return "어제";
        } else if (daysBetween <= 3) {
            return (daysBetween - 1) + "일 전";
        } else {
            return createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
    }
}
