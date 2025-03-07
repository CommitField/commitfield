package cmf.commitField.domain.noti.noti.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class NotiEvent extends ApplicationEvent {
    private final String username;
    private final String message;

    public NotiEvent(Object source, String username, String message) {
        super(source);
        this.username = username;
        this.message = message;
    }
}
