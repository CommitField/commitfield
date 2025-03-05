package cmf.commitField.domain.Timer.entity;

import cmf.commitField.domain.user.entity.User;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Timer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private User user;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String totalTime;

    // 타이머 종료 메서드
    public void stop(LocalDateTime endTime, String totalTime) {
        this.endTime = endTime;
        this.totalTime = totalTime;
    }
}
