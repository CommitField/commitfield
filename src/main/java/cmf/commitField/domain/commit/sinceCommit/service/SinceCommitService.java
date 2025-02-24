package cmf.commitField.domain.commit.sinceCommit.service;

import cmf.commitField.domain.commit.sinceCommit.dto.SinceCommitResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
public class SinceCommitService {
    private static final String BASE_URL = "https://api.github.com";
    // ?since=2024-01-01T00:00:00Z&until=2025-02-1T23:59:59Z

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
                    .bodyToMono(new ParameterizedTypeReference<List<SinceCommitResponseDto>>() {})
                    .block();
        } catch (Exception e) {
            log.error("GitHub API 호출 중 오류 발생: {}", e.getMessage());
            throw new RuntimeException("GitHub API 호출 중 오류가 발생했습니다.", e);
        }
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