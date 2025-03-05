package cmf.commitField.domain.commit.sinceCommit.service;

import cmf.commitField.domain.commit.sinceCommit.dto.CommitAnalysisResponseDto;
import cmf.commitField.domain.commit.sinceCommit.dto.SinceCommitResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SinceCommitService {
    private static final String BASE_URL = "https://api.github.com";

    @Value("${github.token}")
    private String PAT;

    private final WebClient webClient = WebClient.builder()
            .baseUrl(BASE_URL)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();

    public List<SinceCommitResponseDto> getSinceCommits(String owner, String repo, LocalDateTime since, LocalDateTime until) {
        try {
            return webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/repos/{owner}/{repo}/commits")
                            .queryParam("since", since.format(DateTimeFormatter.ISO_DATE_TIME))
                            .queryParam("until", until.format(DateTimeFormatter.ISO_DATE_TIME))
                            .build(owner, repo))
                    .header("Authorization", "bearer " + PAT)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<SinceCommitResponseDto>>() {
                    })
                    .block();
        } catch (Exception e) {
            log.error("GitHub API 호출 중 오류 발생: {}", e.getMessage());
            throw new RuntimeException("GitHub API 호출 중 오류가 발생했습니다.", e);
        }
    }

    // 연속 커밋 수 계산

    public CommitAnalysisResponseDto getCommitAnalysis(String owner, String repo, LocalDateTime since, LocalDateTime until) {
        List<SinceCommitResponseDto> commits = getSinceCommits(owner, repo, since, until);
        StreakResult streakResult = calculateStreaks(commits);
        return new CommitAnalysisResponseDto(commits, streakResult.currentStreak, streakResult.maxStreak);
    }

    @RequiredArgsConstructor
    private static class StreakResult {
        final int currentStreak;
        final int maxStreak;
    }

    private StreakResult calculateStreaks(List<SinceCommitResponseDto> commits) {
        if (commits == null || commits.isEmpty()) {
            return new StreakResult(0, 0);
        }

        // 커밋 날짜를 LocalDate로 변환하고 Set으로 중복 제거
        Set<LocalDate> commitDates = commits.stream()
                .map(commit -> LocalDateTime.parse(
                                commit.getCommit().getAuthor().getDate(),
                                DateTimeFormatter.ISO_DATE_TIME)
                        .toLocalDate())
                .collect(Collectors.toCollection(TreeSet::new));

        // 오늘 날짜 가져오기 (UTC 기준)
        LocalDate today = LocalDate.now(ZoneOffset.UTC);

        // 현재 streak 계산
        int currentStreak = calculateCurrentStreak(commitDates, today);

        // 최대 streak 계산
        int maxStreak = calculateMaxStreak(commitDates);

        return new StreakResult(currentStreak, maxStreak);
    }

    private int calculateCurrentStreak(Set<LocalDate> commitDates, LocalDate today) {
        // 어제 커밋이 없으면 오늘 커밋만 확인
        if (!commitDates.contains(today.minusDays(1))) {
            return commitDates.contains(today) ? 1 : 0;
        }

        int streakCount = 0;
        LocalDate checkDate = today;

        // 오늘부터 역순으로 연속된 커밋이 있는지 확인
        while (commitDates.contains(checkDate)) {
            streakCount++;
            checkDate = checkDate.minusDays(1);
        }

        return streakCount;
    }

    private int calculateMaxStreak(Set<LocalDate> commitDates) {
        if (commitDates.isEmpty()) {
            return 0;
        }

        List<LocalDate> sortedDates = new ArrayList<>(commitDates);
        Collections.sort(sortedDates);

        int maxStreak = 1;
        int currentStreak = 1;

        for (int i = 1; i < sortedDates.size(); i++) {
            if (sortedDates.get(i).minusDays(1).equals(sortedDates.get(i - 1))) {
                currentStreak++;
                maxStreak = Math.max(maxStreak, currentStreak);
            } else {
                currentStreak = 1;
            }
        }

        return maxStreak;
    }
}
    /*
        ParameterizedTypeReference를 사용하는 주요 이유
        1. 제네릭 타입 정보 보존
        2. 복잡한 타입 구조 처리 가능
        3. 타입 안전성 보장
        4. 런타임에 올바른 타입으로 역직렬화 가능

        ParameterizedTypeReference를 사용하지 않으면
        // 잘못된 예시
        List<GitHubCommitResponse> commits = webClient.get()
            .retrieve()
            .bodyToMono(List.class)
            .block();

        // 런타임 에러 발생 가능
        GitHubCommitResponse commit = commits.get(0); // ClassCastException 발생 위험
    */