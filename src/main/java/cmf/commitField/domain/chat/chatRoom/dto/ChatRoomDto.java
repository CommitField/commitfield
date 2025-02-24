package cmf.commitField.domain.chat.chatRoom.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoomDto {
    private Long id;

    private String title;

    private Long currentUserCount;

    private Integer userCountMax;
}