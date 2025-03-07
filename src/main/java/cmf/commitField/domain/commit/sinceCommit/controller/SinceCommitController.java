package cmf.commitField.domain.commit.sinceCommit.controller;

import cmf.commitField.domain.commit.sinceCommit.dto.CommitAnalysisResponseDto;
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

    // 연속 커밋 일수와 같은 추가 분석 정보를 포함하여 반환
    @GetMapping("/api/github/commits-analysis")
    public ResponseEntity<CommitAnalysisResponseDto> getCommitsAnalysis(
            @RequestParam String owner,
            @RequestParam String repo,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime since,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime until
    ) {
        CommitAnalysisResponseDto analysis = sinceCommitService.getCommitAnalysis(owner, repo, since, until);
        return ResponseEntity.ok(analysis);
    }

    // 시즌 별 커밋 정보 불러오기(테스트용으로 2024년 설정)

    // 봄 시즌(3/1 - 5/31)
    @GetMapping("/api/github/commits-spring")
    public ResponseEntity<CommitAnalysisResponseDto> getSpringSeasonCommits(
            @RequestParam String owner,
            @RequestParam String repo
    ) {
        int currentYear = LocalDateTime.now().getYear();
        LocalDateTime since = LocalDateTime.of(2025, 3, 1, 0, 0);
        LocalDateTime until = LocalDateTime.of(2025, 5, 31, 23, 59, 59);

        CommitAnalysisResponseDto analysis = sinceCommitService.getCommitAnalysis(owner, repo, since, until);
        return ResponseEntity.ok(analysis);
    }

    // 여름 시즌(6/1 - 8/31)
    @GetMapping("/api/github/commits-summer")
    public ResponseEntity<CommitAnalysisResponseDto> getSummerSeasonCommits(
            @RequestParam String owner,
            @RequestParam String repo
    ) {
        int currentYear = LocalDateTime.now().getYear();
        LocalDateTime since = LocalDateTime.of(2024, 6, 1, 0, 0);
        LocalDateTime until = LocalDateTime.of(2024, 8, 31, 23, 59, 59);

        CommitAnalysisResponseDto analysis = sinceCommitService.getCommitAnalysis(owner, repo, since, until);
        return ResponseEntity.ok(analysis);
    }

    // 가을 시즌(9/1 - 11/30)
    @GetMapping("/api/github/commits-fall")
    public ResponseEntity<CommitAnalysisResponseDto> getFallSeasonCommits(
            @RequestParam String owner,
            @RequestParam String repo
    ) {
        int currentYear = LocalDateTime.now().getYear();
        LocalDateTime since = LocalDateTime.of(2024, 9, 1, 0, 0);
        LocalDateTime until = LocalDateTime.of(2024, 11, 30, 23, 59, 59);

        CommitAnalysisResponseDto analysis = sinceCommitService.getCommitAnalysis(owner, repo, since, until);
        return ResponseEntity.ok(analysis);
    }

    // 겨울 시즌(이전 년도 12/1 - 다음 년도 2/30)
    @GetMapping("/api/github/commits-winter")
    public ResponseEntity<CommitAnalysisResponseDto> getWinterSeasonCommits(
            @RequestParam String owner,
            @RequestParam String repo
    ) {
        int currentYear = LocalDateTime.now().getYear();
        LocalDateTime since = LocalDateTime.of(2024, 12, 1, 0, 0);
        LocalDateTime until = LocalDateTime.of(2024 + 1, 2, 28, 23, 59, 59)
                .with(TemporalAdjusters.lastDayOfMonth());

        CommitAnalysisResponseDto analysis = sinceCommitService.getCommitAnalysis(owner, repo, since, until);
        return ResponseEntity.ok(analysis);
    }

}
