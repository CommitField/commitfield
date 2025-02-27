package cmf.commitField.domain.redpanda;

import cmf.commitField.domain.redpanda.dto.CommitUpdateMessageDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class RedpandaConsumer {

    private final ObjectMapper objectMapper;

    public RedpandaConsumer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "commit-topic", groupId = "commit-group")
    public void listen(ConsumerRecord<String, String> record) {
        try {
            String message = record.value();
            CommitUpdateMessageDto commitUpdate = objectMapper.readValue(message, CommitUpdateMessageDto.class);
            System.out.println("✅ Received commit update: " + commitUpdate);
        } catch (Exception e) {
            System.err.println("❌ Failed to parse commit update: " + e.getMessage());
        }
    }
}