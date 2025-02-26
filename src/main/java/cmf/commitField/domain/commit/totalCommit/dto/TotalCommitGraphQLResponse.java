package cmf.commitField.domain.commit.totalCommit.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

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
        private long totalCommitContributions;  // 공개 레포 커밋
        private long restrictedContributionsCount;  // 비공개 레포 커밋
        private ContributionCalendar contributionCalendar;
    }

    @Getter
    @NoArgsConstructor
    public static class ContributionCalendar {
        private int totalContributions;
        private List<Week> weeks;
    }

    @Getter
    @NoArgsConstructor
    public static class Week {
        private List<ContributionDay> contributionDays;
    }

    @Getter
    @NoArgsConstructor
    public static class ContributionDay {
        private int contributionCount;
        private String date;
    }
}
