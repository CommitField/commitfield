package cmf.commitField.domain.noti.noti.event;

import cmf.commitField.domain.noti.noti.dto.NotiDto;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.List;

@Getter
public class NotiEvent extends ApplicationEvent {
    private final String username;
    private final List<NotiDto> notis;
    private final String message;

    public NotiEvent(Object source, String username, List<NotiDto> notis, String message) {
        super(source);
        this.username = username;
        this.notis = notis;
        this.message = message;
    }
}
