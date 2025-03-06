package cmf.commitField.domain.commit.scheduler;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class CommitUpdateEvent extends ApplicationEvent {
    private final Long userId;
    private final int commitCount;

    public CommitUpdateEvent(Object source, Long userId, int commitCount) {
        super(source);
        this.userId = userId;
        this.commitCount = commitCount;
    }

}
