package cmf.commitField.domain.season.controller;

import cmf.commitField.domain.season.entity.Season;
import cmf.commitField.domain.season.service.SeasonService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.swing.*;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/seasons")
@RequiredArgsConstructor
public class ApiV1SeasonController {
    private final SeasonService seasonService;

//    입력 예시
//    name: 2025 1분기
//    start: 2025-01-01T00:00:00Z
//    end: 2025-02-28T23:59:59Z
    @PostMapping
    public Season createSeason() {
        String name = "2025 1분기";
        LocalDateTime start = LocalDateTime.of(2025, 1, 1, 0, 0, 0);
        LocalDateTime end = LocalDateTime.of(2025, 2, 28, 23, 59, 59);

        return seasonService.createNewSeason(name, start, end);
    }

    @GetMapping("/active")
    public Season getActiveSeason() {
        return seasonService.getActiveSeason();
    }
}
