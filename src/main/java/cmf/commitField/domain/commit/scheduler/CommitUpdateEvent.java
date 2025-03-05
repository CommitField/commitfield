package cmf.commitField.domain.commit.scheduler;

import org.springframework.context.ApplicationEvent;

public class CommitUpdateEvent extends ApplicationEvent {
    private final Long userId;
    private final int commitCount;

    public CommitUpdateEvent(Object source, Long userId, int commitCount) {
        super(source);
        this.userId = userId;
        this.commitCount = commitCount;
    }

    public Long getUserId() {
        return userId;
    }

    public int getCommitCount() {
        return commitCount;
    }
}
