package cmf.commitField.domain.chat.chatMessage.controller.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMsgResponse {
    //채팅방 ID
    private Long roomId;
    private Long id;
    //사용자(user)
    private String from;
    private String message;
    private LocalDateTime sendAt;
    private String avatarUrl;  // 아바타 URL 필드 추가
}
