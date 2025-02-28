package cmf.commitField.domain.chat.chatRoom.dto;

import cmf.commitField.domain.chat.chatRoom.entity.ChatRoom;
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

    private Integer heartCount;

    public static ChatRoomDto of(ChatRoom chatRoom) {
        return ChatRoomDto.builder()
                .id(chatRoom.getId())
                .title(chatRoom.getTitle())
                .currentUserCount((long) chatRoom.getUserChatRooms().size())
                .userCountMax(chatRoom.getUserCountMax())
                .heartCount(chatRoom.getHearts().size())
                .build();
    }
}