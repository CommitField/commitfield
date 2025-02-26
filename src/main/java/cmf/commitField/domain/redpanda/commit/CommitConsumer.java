package cmf.commitField.domain.redpanda.commit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class CommitConsumer {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @KafkaListener(topics = "commit-events", groupId = "commit-group")
    public void consume(String message) {
        System.out.println("Received commit event: " + message);
        // WebSocket을 통해 Frontend로 전송
        messagingTemplate.convertAndSend("/topic/commits", message);
    }

}