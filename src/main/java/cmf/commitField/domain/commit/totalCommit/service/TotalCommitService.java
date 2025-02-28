package cmf.commitField.domain.commit.totalCommit.service;

import cmf.commitField.domain.commit.totalCommit.dto.TotalCommitGraphQLResponse;
import cmf.commitField.domain.commit.totalCommit.dto.TotalCommitResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class TotalCommitService {
    private static final String BASE_URL = "https://api.github.com/graphql";

    @Value("${github.token}")
    private String PAT;


    private final WebClient webClient = WebClient.builder()
            .baseUrl(BASE_URL)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();

//    // 기존 메서드
//    public TotalCommitResponseDto getTotalCommitCount(String username) {
//        Map<String, String> requestBody = Map.of(
//                "query", String.format(
//                        "query { user(login: \"%s\") { contributionsCollection { totalCommitContributions restrictedContributionsCount } } }",
//                        username
//                )
//        );
//
//        TotalCommitGraphQLResponse response = webClient.post()
//                .header("Authorization", "bearer " + PAT)
//                .bodyValue(requestBody)
//                .retrieve()
//                .bodyToMono(TotalCommitGraphQLResponse.class)
//                .block();
//
//        TotalCommitGraphQLResponse.ContributionsCollection contributions =
//                response.getData().getUser().getContributionsCollection();
//
//        return new TotalCommitResponseDto(
//                contributions.getTotalCommitContributions(),
//                contributions.getRestrictedContributionsCount()
//        );
//        // streak 계산 부분 추가
//        List<LocalDate> commitDates = extractCommitDates(contributions.getContributionCalendar());
//        StreakResult streaks = calculateStreaks(commitDates);
//
//        return new TotalCommitResponseDto(
//                contributions.getTotalCommitContributions(),
//                contributions.getRestrictedContributionsCount(),
//                streaks.currentStreak,
//                streaks.maxStreak
//        );
//    }

    // 연속 커밋 수 정보도 같이 반환
    public TotalCommitResponseDto getTotalCommitCount(String username) {
        // GraphQL 쿼리를 수정하여 contributionCalendar도 함께 요청
        String query = String.format(
                "query { user(login: \"%s\") { contributionsCollection { " +
                        "totalCommitContributions restrictedContributionsCount " +
                        "contributionCalendar { totalContributions weeks { contributionDays { contributionCount date } } } " +
                        "} } }",
                username
        );

        Map<String, String> requestBody = Map.of("query", query);

        TotalCommitGraphQLResponse response = webClient.post()
                .header("Authorization", "bearer " + PAT)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(TotalCommitGraphQLResponse.class)
                .block();

        if (response == null || response.getData() == null || response.getData().getUser() == null) {
            throw new RuntimeException("Failed to fetch GitHub data");
        }

        TotalCommitGraphQLResponse.ContributionsCollection contributions =
                response.getData().getUser().getContributionsCollection();

        // streak 기본값 설정
        int currentStreak = 0;
        int maxStreak = 0;

        // contributionCalendar가 존재하는 경우에만 streak 계산
        if (contributions.getContributionCalendar() != null) {
            List<LocalDate> commitDates = extractCommitDates(contributions.getContributionCalendar());
            if (!commitDates.isEmpty()) {
                StreakResult streaks = calculateStreaks(commitDates);
                currentStreak = streaks.currentStreak;
                maxStreak = streaks.maxStreak;
            }
        }

        return new TotalCommitResponseDto(
                contributions.getTotalCommitContributions(),
                contributions.getRestrictedContributionsCount(),
                currentStreak,
                maxStreak
        );
    }

    // 시즌별 커밋 분석
    public TotalCommitResponseDto getSeasonCommits(String username, LocalDateTime since, LocalDateTime until) {
        String query = String.format("""
            query {
                user(login: "%s") {
                    contributionsCollection(from: "%s", to: "%s") {
                        totalCommitContributions
                        restrictedContributionsCount
                        contributionCalendar {
                            totalContributions
                            weeks {
                                contributionDays {
                                    contributionCount
                                    date
                                }
                            }
                        }
                    }
                }
            }
        """, username, since.format(DateTimeFormatter.ISO_DATE_TIME), until.format(DateTimeFormatter.ISO_DATE_TIME));

        Map<String, String> requestBody = Map.of("query", query);

        TotalCommitGraphQLResponse response = webClient.post()
                .header("Authorization", "bearer " + PAT)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(TotalCommitGraphQLResponse.class)
                .block();

        if (response == null || response.getData() == null || response.getData().getUser() == null) {
            throw new RuntimeException("Failed to fetch GitHub data");
        }

        TotalCommitGraphQLResponse.ContributionsCollection contributions =
                response.getData().getUser().getContributionsCollection();

        List<LocalDate> commitDates = extractCommitDates(contributions.getContributionCalendar());
        StreakResult streaks = calculateStreaks(commitDates);

        return new TotalCommitResponseDto(
                contributions.getTotalCommitContributions(),
                contributions.getRestrictedContributionsCount(),
                streaks.currentStreak,
                streaks.maxStreak
        );
    }

    private List<LocalDate> extractCommitDates(TotalCommitGraphQLResponse.ContributionCalendar calendar) {
        List<LocalDate> dates = new ArrayList<>();
        calendar.getWeeks().forEach(week ->
                week.getContributionDays().forEach(day -> {
                    if (day.getContributionCount() > 0) {
                        dates.add(LocalDate.parse(day.getDate()));
                    }
                })
        );
        return dates;
    }

    @RequiredArgsConstructor
    private static class StreakResult {
        final int currentStreak;
        final int maxStreak;
    }

    private StreakResult calculateStreaks(List<LocalDate> commitDates) {
        if (commitDates.isEmpty()) {
            return new StreakResult(0, 0);
        }

        Collections.sort(commitDates);
        int currentStreak = 0;
        int maxStreak = 0;
        int tempStreak = 0;
        LocalDate previousDate = null;

        for (LocalDate date : commitDates) {
            if (previousDate == null || date.minusDays(1).equals(previousDate)) {
                tempStreak++;
            } else {
                tempStreak = 1;
            }
            maxStreak = Math.max(maxStreak, tempStreak);
            previousDate = date;
        }

        // 현재 스트릭 계산 (마지막 커밋이 오늘 또는 어제인 경우)
        LocalDate today = LocalDate.now();
        LocalDate lastCommitDate = commitDates.get(commitDates.size() - 1);
        if (lastCommitDate.equals(today) || lastCommitDate.equals(today.minusDays(1))) {
            currentStreak = tempStreak;
        }

        return new StreakResult(currentStreak, maxStreak);
    }

    // 시간별 커밋 분석
    public TotalCommitResponseDto getUpdateCommits(String username, LocalDateTime since, LocalDateTime until) {
        String query = String.format("""
        query {
            user(login: "%s") {
                contributionsCollection(from: "%s", to: "%s") {
                    commitContributionsByRepository {
                        contributions(first: 100) {
                            nodes {
                                occurredAt  # ✅ 시간 정보 포함
                            }
                        }
                    }
                }
            }
        }""", username, since.format(DateTimeFormatter.ISO_DATE_TIME), until.format(DateTimeFormatter.ISO_DATE_TIME));

        Map<String, String> requestBody = Map.of("query", query);

        TotalCommitGraphQLResponse response = webClient.post()
                .header("Authorization", "bearer " + PAT)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(TotalCommitGraphQLResponse.class)
                .block();

        if (response == null || response.getData() == null || response.getData().getUser() == null) {
            throw new RuntimeException("Failed to fetch GitHub data");
        }

        TotalCommitGraphQLResponse.ContributionsCollection contributions =
                response.getData().getUser().getContributionsCollection();

        return new TotalCommitResponseDto(
                contributions.getTotalCommitContributions(),
                contributions.getRestrictedContributionsCount()
        );
    }
}
