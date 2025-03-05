package cmf.commitField.domain.season.controller;

import cmf.commitField.domain.season.entity.Season;
import cmf.commitField.domain.season.service.SeasonService;
import cmf.commitField.global.scheduler.SeasonScheduler;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/seasons")
@RequiredArgsConstructor
public class ApiV1SeasonController {
    private final SeasonService seasonService;
    private final SeasonScheduler seasonScheduler;

    // 예시로 Season 데이터 입력 해보기
    @PostMapping
    public Season createSeason() {
        String name = "2025 1분기";
        LocalDateTime start = LocalDateTime.of(2024, 12, 1, 0, 0, 0);
        LocalDateTime end = LocalDateTime.of(2025, 2, 28, 23, 59, 59);

        return seasonService.createNewSeason(name, start, end);
    }

    @GetMapping("/active")
    public Season getActiveSeason() {
        return seasonService.getActiveSeason();
    }
}
