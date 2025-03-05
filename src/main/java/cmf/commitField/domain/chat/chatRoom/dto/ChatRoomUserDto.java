package cmf.commitField.domain.chat.chatRoom.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoomUserDto {
    private String nickname;
    private Boolean status;
}
