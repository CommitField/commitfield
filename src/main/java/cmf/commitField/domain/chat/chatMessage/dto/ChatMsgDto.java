package cmf.commitField.domain.chat.chatMessage.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMsgDto {
    private Long chatMsgId;
    private Long userId;
    private String nickname;
    private String message;
    private LocalDateTime sendAt;
}
