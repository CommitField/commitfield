package cmf.commitField.domain.Timer.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TimerDto {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String totalTime;
}
