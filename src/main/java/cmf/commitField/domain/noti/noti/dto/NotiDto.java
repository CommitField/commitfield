package cmf.commitField.domain.noti.noti.dto;

import cmf.commitField.domain.noti.noti.entity.Noti;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@Getter
public class NotiDto {
    private String message;
    private String formattedCreatedAt; // 변환된 날짜를 저장할 필드

    public NotiDto(Noti noti) {
        this.message = noti.getMessage();
        this.formattedCreatedAt = formatCreatedAt(noti.getCreatedAt()); // 변환된 날짜 저장
    }

    private String formatCreatedAt(LocalDateTime createdAt) {
        LocalDateTime today = LocalDateTime.now();
        long daysBetween = ChronoUnit.DAYS.between(createdAt, today);

        if (daysBetween == 0) {
            return "오늘";
        } else if (daysBetween == 1) {
            return "어제";
        } else if (daysBetween == 2) {
            return "1일 전";
        } else if (daysBetween == 3) {
            return "2일 전";
        } else if (daysBetween == 4) {
            return "3일 전";
        } else {
            return createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
    }
}
