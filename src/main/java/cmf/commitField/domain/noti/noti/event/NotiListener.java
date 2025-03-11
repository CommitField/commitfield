package cmf.commitField.domain.noti.noti.event;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotiListener {
    private final SimpMessagingTemplate messagingTemplate;
    @EventListener
    public void handleNotiEvent(NotiEvent event) {
        String username = event.getUsername();
        System.out.println("NotiEvent: " + event.getMessage());
        messagingTemplate.convertAndSend("/topic/notifications/" + username, event.getNotis());
    }
}
