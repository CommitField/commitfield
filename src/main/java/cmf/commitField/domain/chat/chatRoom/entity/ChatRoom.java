package cmf.commitField.domain.chat.chatRoom.entity;

import cmf.commitField.domain.chat.chatMessage.entity.ChatMessage;
import cmf.commitField.domain.chat.userChatRoom.entity.UserChatRoom;
import cmf.commitField.domain.user.entity.User;
import cmf.commitField.global.jpa.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoom extends BaseEntity {

    private String title;

    private LocalDateTime deletedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "chatRoom", fetch = FetchType.LAZY)
    private List<UserChatRoom> userChatRooms;

    @OneToMany(mappedBy = "chatRoom", fetch = FetchType.LAZY)
    private List<ChatMessage> chatMessages;
}