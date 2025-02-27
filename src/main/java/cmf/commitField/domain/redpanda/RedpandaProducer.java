package cmf.commitField.domain.redpanda;

import cmf.commitField.domain.redpanda.dto.CommitUpdateMessageDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedpandaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper; // JSON 변환을 위한 ObjectMapper
    private static final String TOPIC = "commit-topic";

    public RedpandaProducer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    // 🔹 커밋 업데이트 전송 메서드
    public void sendCommitUpdate(String username, long commitCount) {
        try {
            CommitUpdateMessageDto message = new CommitUpdateMessageDto(username, commitCount);
            String jsonMessage = objectMapper.writeValueAsString(message); // DTO를 JSON으로 변환
            kafkaTemplate.send(TOPIC, jsonMessage);
            System.out.println("📨 Sent commit update to Redpanda: " + jsonMessage);
        } catch (Exception e) {
            System.err.println("❌ Failed to send commit update: " + e.getMessage());
        }
    }
}