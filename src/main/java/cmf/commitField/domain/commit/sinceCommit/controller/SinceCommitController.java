package cmf.commitField.domain.commit.sinceCommit.controller;

import cmf.commitField.domain.commit.sinceCommit.dto.SinceCommitResponseDto;
import cmf.commitField.domain.commit.sinceCommit.service.SinceCommitService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class SinceCommitController {
    private final SinceCommitService sinceCommitService;

    @GetMapping("/api/github/commits-since")
    public ResponseEntity<List<SinceCommitResponseDto>> getCommits(
            @RequestParam String owner,
            @RequestParam String repo,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime since,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime until
            ) {
        List<SinceCommitResponseDto> sinceCommits = sinceCommitService.getSinceCommits(owner, repo, since, until);

        return ResponseEntity.ok(sinceCommits);
    }

    // 시즌 별 커밋 수 불러오기

    // 봄 시즌
    @GetMapping("/api/github/commits-spring")
    public ResponseEntity<List<SinceCommitResponseDto>> getSpringSeasonCommits(
            @RequestParam String owner,
            @RequestParam String repo
    ) {
        int currentYear = LocalDateTime.now().getYear();
        LocalDateTime since = LocalDateTime.of(2024, 3, 1, 0, 0);
        LocalDateTime until = LocalDateTime.of(2024, 5, 31, 23, 59, 59);

        List<SinceCommitResponseDto> sinceCommits = sinceCommitService.getSinceCommits(owner, repo, since, until);

        return ResponseEntity.ok(sinceCommits);
    }

    // 여름 시즌
    @GetMapping("/api/github/commits-summer")
    public ResponseEntity<List<SinceCommitResponseDto>> getSummerSeasonCommits(
            @RequestParam String owner,
            @RequestParam String repo
    ) {
        int currentYear = LocalDateTime.now().getYear();
        LocalDateTime since = LocalDateTime.of(currentYear, 6, 1, 0, 0);
        LocalDateTime until = LocalDateTime.of(currentYear, 8, 31, 23, 59, 59);

        List<SinceCommitResponseDto> sinceCommits = sinceCommitService.getSinceCommits(owner, repo, since, until);

        return ResponseEntity.ok(sinceCommits);
    }

    // 가을 시즌
    @GetMapping("/api/github/commits-fall")
    public ResponseEntity<List<SinceCommitResponseDto>> getFallSeasonCommits(
            @RequestParam String owner,
            @RequestParam String repo
    ) {
        int currentYear = LocalDateTime.now().getYear();
        LocalDateTime since = LocalDateTime.of(currentYear, 9, 1, 0, 0);
        LocalDateTime until = LocalDateTime.of(currentYear, 11, 30, 23, 59, 59);

        List<SinceCommitResponseDto> sinceCommits = sinceCommitService.getSinceCommits(owner, repo, since, until);

        return ResponseEntity.ok(sinceCommits);
    }

    // 겨울 시즌
    @GetMapping("/api/github/commits-winter")
    public ResponseEntity<List<SinceCommitResponseDto>> getWinterSeasonCommits(
            @RequestParam String owner,
            @RequestParam String repo
    ) {
        int currentYear = LocalDateTime.now().getYear();
        LocalDateTime since = LocalDateTime.of(currentYear, 12, 1, 0, 0);
        LocalDateTime until = LocalDateTime.of(currentYear + 1, 2, 1, 23, 59, 59)
                .with(TemporalAdjusters.lastDayOfMonth()); // 윤년 고려

        List<SinceCommitResponseDto> sinceCommits = sinceCommitService.getSinceCommits(owner, repo, since, until);

        return ResponseEntity.ok(sinceCommits);
    }
}
