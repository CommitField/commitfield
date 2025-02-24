package cmf.commitField.domain.commit.totalCommit.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TotalCommitResponseDto {
    private long totalCommitContributions;
    private long restrictedContributionsCount;
}
