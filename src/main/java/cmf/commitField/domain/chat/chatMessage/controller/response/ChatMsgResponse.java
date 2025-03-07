package cmf.commitField.domain.chat.chatMessage.controller.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMsgResponse {
    private Long chatMsgId;
    private Long roomId;
    //사용자(user)
    private String from;
    private String message;
    private LocalDateTime sendAt;
}
