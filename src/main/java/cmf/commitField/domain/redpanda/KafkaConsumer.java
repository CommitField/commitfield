package cmf.commitField.domain.redpanda;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {

    @KafkaListener(topics = "commit-topic", groupId = "commit-group")
    public void listen(String message) {
        System.out.println("Received message: " + message);
    }
}