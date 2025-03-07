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

    // 이미지 URL 필드 추가
    private String imageUrl;

    public static ChatRoomDto of(ChatRoom chatRoom) {
        return ChatRoomDto.builder()
                .id(chatRoom.getId())
                .title(chatRoom.getTitle())
                .currentUserCount((long) chatRoom.getUserChatRooms().size())
                .userCountMax(chatRoom.getUserCountMax())
                .heartCount(chatRoom.getHearts().size())
                .imageUrl(chatRoom.getImageUrl())  // ChatRoom에서 imageUrl을 가져오기
                .build();
    }
}