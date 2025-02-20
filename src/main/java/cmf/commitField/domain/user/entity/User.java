package cmf.commitField.domain.user.entity;

import cmf.commitField.domain.chat.chatMessage.entity.ChatMessage;
import cmf.commitField.domain.chat.chatRoom.entity.ChatRoom;
import cmf.commitField.domain.chat.userChatRoom.entity.UserChatRoom;
import cmf.commitField.global.jpa.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {
    private String username; // GitHub 로그인 ID
    private String email; // 이메일
    private String nickname; // 닉네임
    private String avatarUrl; //아바타

    @Enumerated(EnumType.STRING)  // DB에 저장될 때 String 형태로 저장됨
    private Role role;

    public enum Role {
        USER, ADMIN
    }

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<ChatRoom> chatRooms;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<UserChatRoom> userChatRooms;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<ChatMessage> chatMessages;
}
