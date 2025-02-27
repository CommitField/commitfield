package cmf.commitField.domain.noti.noti.eventListener;

import cmf.commitField.domain.noti.noti.entity.NotiDetailType;
import cmf.commitField.domain.noti.noti.entity.NotiType;
import cmf.commitField.domain.noti.noti.service.NotiService;
import cmf.commitField.global.chat.ChatMessageDto;
import cmf.commitField.global.event.CommitHistoryEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotiEventListener {
    // 이벤트가 호출될 때 실행되는 메서드
    // 메서드를 별도의 스레드에서 비동기적으로 실행하도록 만듦
    private final NotiService notiService;

    // 예시
    // CommitHistoryEvent 이벤트가 발생하면 notiService.createCommitStreak() 메서드를 실행
    public void listenCommitStreak(CommitHistoryEvent event){
        notiService.createCommitStreak(event.getUsername(), NotiType.STREAK, NotiDetailType.STREAK_CONTINUED);
    }

    @KafkaListener(topics = "chat-room-1", groupId = "1")
    public void consume(ChatMessageDto message){
        System.out.println("Consumed message: " + message);
    }

    @KafkaListener(topics = "chat-room-1-dlt", groupId = "1")
    public void consumeChatRoom1DLT(byte[] in){
        String message = new String(in);
        System.out.println("Failed message: " + message);
    }
}
