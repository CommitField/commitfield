package cmf.commitField.domain.commit.totalCommit.controller;

import cmf.commitField.domain.commit.totalCommit.dto.TotalCommitResponseDto;
import cmf.commitField.domain.commit.totalCommit.service.TotalCommitService;
import cmf.commitField.domain.user.entity.CustomOAuth2User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;

@RestController
@RequiredArgsConstructor
public class TotalCommitController {
    private final TotalCommitService totalCommitService;

//    @GetMapping("/api/commits/{username}")
//    public TotalCommitResponseDto getTotalCommits(@PathVariable String username) {
//        return totalCommitService.getTotalCommitCount(username);
//    }

    // 로그인한 사용자의 username 이용
    @GetMapping("/api/commits")
    public TotalCommitResponseDto getTotalCommits(@AuthenticationPrincipal CustomOAuth2User oAuth2User) {
        String username = oAuth2User.getName();  // CustomOAuth2User의 getName()은 user.getUsername()을 반환
        return totalCommitService.getTotalCommitCount(username);
    }

    // 봄 시즌(3/1 - 5/31)
//    @GetMapping("/api/commits/{username}/spring")
//    public TotalCommitResponseDto getSpringSeasonCommits(@PathVariable String username) {
//        int currentYear = LocalDateTime.now().getYear();  // 현재는 테스트용으로 2024 대입
//        LocalDateTime since = LocalDateTime.of(2024, 3, 1, 0, 0);
//        LocalDateTime until = LocalDateTime.of(2024, 5, 31, 23, 59, 59);
//        return totalCommitService.getSeasonCommits(username, since, until);
//    }

    // 마찬가지로 로그인한 사용자의 username 이용
    @GetMapping("/api/commits/spring")
    public TotalCommitResponseDto getSpringSeasonCommits(@AuthenticationPrincipal CustomOAuth2User oAuth2User) {
        String username = oAuth2User.getName();
        int currentYear = LocalDateTime.now().getYear();  // 올해 대입
        LocalDateTime since = LocalDateTime.of(2025, 3, 1, 0, 0);
        LocalDateTime until = LocalDateTime.of(2025, 5, 31, 23, 59, 59);
        return totalCommitService.getSeasonCommits(username, since, until);
    }

    // 여름 시즌(6/1 - 8/31)
    @GetMapping("/api/commits/summer")
    public TotalCommitResponseDto getSummerSeasonCommits(@AuthenticationPrincipal CustomOAuth2User oAuth2User) {
        String username = oAuth2User.getName();
        int currentYear = LocalDateTime.now().getYear();  // 현재는 테스트용으로 2024 대입
        LocalDateTime since = LocalDateTime.of(2024, 6, 1, 0, 0);
        LocalDateTime until = LocalDateTime.of(2024, 8, 31, 23, 59, 59);
        return totalCommitService.getSeasonCommits(username, since, until);
    }

    // 가을 시즌(9/1 - 11/30)
    @GetMapping("/api/commits/fall")
    public TotalCommitResponseDto getFallSeasonCommits(@AuthenticationPrincipal CustomOAuth2User oAuth2User) {
        String username = oAuth2User.getName();
        int currentYear = LocalDateTime.now().getYear();  // 현재는 테스트용으로 2024 대입
        LocalDateTime since = LocalDateTime.of(2024, 9, 1, 0, 0);
        LocalDateTime until = LocalDateTime.of(2024, 11, 30, 23, 59, 59);
        return totalCommitService.getSeasonCommits(username, since, until);
    }

    // 겨울 시즌(이전 년도 12/1 - 다음 년도 2/28)
    @GetMapping("/api/commits/winter")
    public TotalCommitResponseDto getWinterSeasonCommits(@AuthenticationPrincipal CustomOAuth2User oAuth2User) {
        String username = oAuth2User.getName();
        int currentYear = LocalDateTime.now().getYear();  // 2024.12 ~ 2025.2 대입
        LocalDateTime since = LocalDateTime.of(2025 - 1, 12, 1, 0, 0);
        LocalDateTime until = LocalDateTime.of(2025, 2, 1, 23, 59, 59)
                .with(TemporalAdjusters.lastDayOfMonth());
        return totalCommitService.getSeasonCommits(username, since, until);
    }
}
