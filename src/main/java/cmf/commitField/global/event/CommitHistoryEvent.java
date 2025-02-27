package cmf.commitField.global.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

// event 객체 참고
@Getter
public class CommitHistoryEvent extends ApplicationEvent {
    private final String username;
    public CommitHistoryEvent(Object source, String username) {
        super(source);
        this.username = username;
    }
}
