package cmf.commitField.domain.totalCommit.service;

import cmf.commitField.domain.totalCommit.dto.TotalCommitGraphQLResponse;
import cmf.commitField.domain.totalCommit.dto.TotalCommitResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;


@Service
@RequiredArgsConstructor
public class TotalCommitService {
    private static final String BASE_URL = "https://api.github.com/graphql";

    @Value("${github.token}")
    private String PAT;


    private final WebClient webClient = WebClient.builder()
            .baseUrl(BASE_URL)
            .defaultHeader(HttpHeaders.CONTENT_TYPE , MediaType.APPLICATION_JSON_VALUE)
            .build();

    public TotalCommitResponseDto getTotalCommitCount(String username) {
//        String query = String.format("""
//                {"query": "query { user(login: \\\"%s\\\") { contributionsCollection { totalCommitContributions restrictedContributionsCount } } }"}
//                """, username);
        // GraphQL 쿼리를 Map으로 구성
        Map<String, String> requestBody = Map.of(
                "query", String.format(
                        "query { user(login: \"%s\") { contributionsCollection { totalCommitContributions restrictedContributionsCount } } }",
                        username
                )
        );

        TotalCommitGraphQLResponse response = webClient.post()
                .header("Authorization", "bearer " + PAT)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(TotalCommitGraphQLResponse.class)
                .block();

        TotalCommitGraphQLResponse.ContributionsCollection contributions = response.getData().getUser().getContributionsCollection();

        return new TotalCommitResponseDto(
                contributions.getTotalCommitContributions(),
                contributions.getRestrictedContributionsCount()
        );
    }
}
