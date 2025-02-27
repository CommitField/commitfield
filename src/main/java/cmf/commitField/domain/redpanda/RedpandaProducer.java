package cmf.commitField.domain.redpanda;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedpandaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private static final String TOPIC = "commit-topic"; // Redpanda에서 사용할 토픽명

    public RedpandaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    // 메시지 전송 메서드
    public void sendMessage(String message) {
        kafkaTemplate.send(TOPIC, message);
        System.out.println("📨 Sent message to Redpanda: " + message);
    }

    // 커밋 업데이트 전송 메서드
    public void sendCommitUpdate(String username, long commitCount) {
        String message = String.format("{\"user\": \"%s\", \"update-commits\": %d}", username, commitCount);
        kafkaTemplate.send(TOPIC, message);
        System.out.println("📨 Sent commit update to Redpanda: " + message);
    }
}