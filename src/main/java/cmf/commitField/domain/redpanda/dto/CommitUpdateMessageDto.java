package cmf.commitField.domain.redpanda.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommitUpdateMessageDto {
    @JsonProperty("user")
    private String username;

    @JsonProperty("update-commits")
    private long commitCount;

    @Override
    public String toString() {
        return "CommitUpdateMessage{" +
                "username='" + username + '\'' +
                ", updateCommitCount=" + commitCount +
                '}';
    }
}
