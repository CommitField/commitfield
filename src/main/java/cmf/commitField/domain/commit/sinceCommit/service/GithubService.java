package cmf.commitField.domain.commit.sinceCommit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GithubService {
    private final RestTemplate restTemplate;
    private final String GITHUB_API_URL = "https://api.github.com";

    @Value("${github.token}")
    private String GITHUB_TOKEN;

    public int getUserCommitCount(String username) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + GITHUB_TOKEN);
        headers.set("Accept", "application/vnd.github.v3+json"); // 최신 GitHub API 버전 지정

        HttpEntity<String> entity = new HttpEntity<>(headers);
        String url = String.format("%s/users/%s/events", GITHUB_API_URL, username);

        // 📌 API 호출 횟수 확인용 로그 추가
        System.out.println("GitHub API 호출: " + url);

        try {
            ResponseEntity<List<Map<String, Object>>> response =
                    restTemplate.exchange(url, HttpMethod.GET, entity, new ParameterizedTypeReference<>() {});

            // GitHub API Rate Limit 확인 (남은 요청 횟수 로깅)
            HttpHeaders responseHeaders = response.getHeaders();
            String remainingRequests = responseHeaders.getFirst("X-RateLimit-Remaining");
            System.out.println("GitHub API 남은 요청 횟수: " + remainingRequests);

            int commitCount = 0;
            if (response.getBody() != null) {
                for (Map<String, Object> event : response.getBody()) {
                    if ("PushEvent".equals(event.get("type"))) {
                        Map<String, Object> payload = (Map<String, Object>) event.get("payload");
                        if (payload != null && payload.containsKey("commits")) {
                            List<?> commits = (List<?>) payload.get("commits");
                            commitCount += (commits != null) ? commits.size() : 0;
                        }
                    }
                }
            }
            return commitCount;

        } catch (HttpClientErrorException e) {
            System.err.println("GitHub API 요청 실패: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                throw new RuntimeException("GitHub API 인증 실패: 올바른 토큰을 사용하세요.");
            } else if (e.getStatusCode() == HttpStatus.FORBIDDEN) {
                throw new RuntimeException("GitHub API 요청 제한 초과 (Rate Limit 초과). 잠시 후 다시 시도하세요.");
            }
            return 0; // 기본값 반환
        } catch (Exception e) {
            System.err.println("예기치 않은 오류 발생: " + e.getMessage());
            return 0; // 기본값 반환
        }
    }
}
