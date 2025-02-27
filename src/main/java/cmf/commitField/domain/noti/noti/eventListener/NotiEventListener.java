package cmf.commitField.domain.noti.noti.eventListener;

import cmf.commitField.domain.noti.noti.service.NotiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotiEventListener {
    private final NotiService notiService;

//    public void listenPost(PostCreatedEvent event){
//        notiService.postCreated(event.getPost());
//    }
//
//    public void consume(ChatMessageDto message){
//        System.out.println("Consumed message: " + message);
//    }
//
//    public void consumeChatRoom1DLT(byte[] in){
//        String message = new String(in);
//        System.out.println("Failed message: " + message);
//    }
}
