package cmf.commitField.domain.commit.totalCommit.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TotalCommitResponseDto {
    private long totalCommitContributions;
    private long restrictedContributionsCount;
    private int currentStreakDays;
    private int maxStreakDays;

    public TotalCommitResponseDto(long totalCommitContributions, long restrictedContributionsCount, int currentStreakDays, int maxStreakDays) {
        this.totalCommitContributions = totalCommitContributions;
        this.restrictedContributionsCount = restrictedContributionsCount;
        this.currentStreakDays = currentStreakDays;
        this.maxStreakDays = maxStreakDays;
    }

    public TotalCommitResponseDto(long totalCommitContributions, long restrictedContributionsCount) {
        this.totalCommitContributions = totalCommitContributions;
        this.restrictedContributionsCount = restrictedContributionsCount;
    }
}
