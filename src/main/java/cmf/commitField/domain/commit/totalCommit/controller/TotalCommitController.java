package cmf.commitField.domain.commit.totalCommit.controller;

import cmf.commitField.domain.commit.totalCommit.dto.TotalCommitResponseDto;
import cmf.commitField.domain.commit.totalCommit.service.TotalCommitService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;

@RestController
@RequiredArgsConstructor
public class TotalCommitController {
    private final TotalCommitService totalCommitService;

    @GetMapping("/api/commits/{username}")
    public TotalCommitResponseDto getTotalCommits(@PathVariable String username) {
        return totalCommitService.getTotalCommitCount(username);
    }

    // 봄 시즌(3/1 - 5/31)
    @GetMapping("/api/commits/{username}/spring")
    public TotalCommitResponseDto getSpringSeasonCommits(@PathVariable String username) {
        int currentYear = LocalDateTime.now().getYear();  // 현재는 테스트용으로 2024 대입
        LocalDateTime since = LocalDateTime.of(2024, 3, 1, 0, 0);
        LocalDateTime until = LocalDateTime.of(2024, 5, 31, 23, 59, 59);
        return totalCommitService.getSeasonCommits(username, since, until);
    }

    // 여름 시즌(6/1 - 8/31)
    @GetMapping("/api/commits/{username}/summer")
    public TotalCommitResponseDto getSummerSeasonCommits(@PathVariable String username) {
        int currentYear = LocalDateTime.now().getYear();  // 현재는 테스트용으로 2024 대입
        LocalDateTime since = LocalDateTime.of(2024, 6, 1, 0, 0);
        LocalDateTime until = LocalDateTime.of(2024, 8, 31, 23, 59, 59);
        return totalCommitService.getSeasonCommits(username, since, until);
    }

    // 가을 시즌(9/1 - 11/30)
    @GetMapping("/api/commits/{username}/fall")
    public TotalCommitResponseDto getFallSeasonCommits(@PathVariable String username) {
        int currentYear = LocalDateTime.now().getYear();  // 현재는 테스트용으로 2024 대입
        LocalDateTime since = LocalDateTime.of(2024, 9, 1, 0, 0);
        LocalDateTime until = LocalDateTime.of(2024, 11, 30, 23, 59, 59);
        return totalCommitService.getSeasonCommits(username, since, until);
    }

    // 겨울 시즌(이전 년도 12/1 - 다음 년도 2/28)
    @GetMapping("/api/commits/{username}/winter")
    public TotalCommitResponseDto getWinterSeasonCommits(@PathVariable String username) {
        int currentYear = LocalDateTime.now().getYear();  // 현재는 테스트용으로 2024 대입
        LocalDateTime since = LocalDateTime.of(2024 - 1, 12, 1, 0, 0);
        LocalDateTime until = LocalDateTime.of(2024, 2, 1, 23, 59, 59)
                .with(TemporalAdjusters.lastDayOfMonth());
        return totalCommitService.getSeasonCommits(username, since, until);
    }
}
