package cmf.commitField.domain.commit.totalCommit.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CommitUpdateDTO {
    long commits;
}
