package cmf.commitField.domain.commit.sinceCommit.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CommitAnalysisResponseDto {
    private final List<SinceCommitResponseDto> commits;
    private final int totalCommits;
    private final int currentStreakDays;
    private final int maxStreakDays;

    public CommitAnalysisResponseDto(List<SinceCommitResponseDto> commits, int currentStreakDays, int maxStreakDays) {
        this.commits = commits;
        this.totalCommits = commits.size();
        this.currentStreakDays = currentStreakDays;
        this.maxStreakDays = maxStreakDays;
    }
}
