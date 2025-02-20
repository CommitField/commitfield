package cmf.commitField.domain.user.entity;

import cmf.commitField.domain.chat.chatMessage.entity.ChatMessage;
import cmf.commitField.domain.chat.chatRoom.entity.ChatRoom;
import cmf.commitField.domain.chat.userChatRoom.entity.UserChatRoom;
import cmf.commitField.global.jpa.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {
    private String email;
    private String nickname;
    private String password;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<ChatRoom> chatRooms;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<UserChatRoom> userChatRooms;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<ChatMessage> chatMessages;
}
