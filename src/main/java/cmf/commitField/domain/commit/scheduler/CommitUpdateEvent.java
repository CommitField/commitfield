package cmf.commitField.domain.commit.scheduler;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class CommitUpdateEvent extends ApplicationEvent {
    private final String username;
    private final long newCommitCount;

    public CommitUpdateEvent(Object source, String username, long newCommitCount) {
        super(source);
        this.username = username;
        this.newCommitCount = newCommitCount;
    }
}
