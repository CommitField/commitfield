package cmf.commitField.domain.totalCommit.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

/*
응답구조 토대로 구현
{
    "data": {
        "user": {
            "contributionsCollection": {
                "totalCommitContributions": 116,
                "restrictedContributionsCount": 0
            }
        }
    }
}
*/
@Getter
@NoArgsConstructor
public class TotalCommitGraphQLResponse {
    private Data data;

    @Getter
    @NoArgsConstructor
    public static class Data {
        CommitUser user;
    }


    @Getter
    @NoArgsConstructor
    public static class CommitUser {
        private ContributionsCollection contributionsCollection;
    }

    @Getter
    @NoArgsConstructor
    public static class ContributionsCollection {
        private long totalCommitContributions;
        private long restrictedContributionsCount;
    }
}
