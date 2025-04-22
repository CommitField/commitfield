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
    // 사용자 github image
    private String imageUrl;
}
