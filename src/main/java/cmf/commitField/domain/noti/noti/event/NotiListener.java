package cmf.commitField.domain.noti.noti.event;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotiListener {
    @EventListener
    public void handleNotiEvent(NotiEvent event) {
        System.out.println("NotiEvent: " + event.getMessage());
    }
}
