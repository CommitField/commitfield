package cmf.commitField.domain.chat.chatRoom.entity;

import cmf.commitField.domain.chat.chatMessage.entity.ChatMsg;
import cmf.commitField.domain.chat.userChatRoom.entity.UserChatRoom;
import cmf.commitField.domain.heart.entity.Heart;
import cmf.commitField.domain.user.entity.User;
import cmf.commitField.global.jpa.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@SuperBuilder
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom extends BaseEntity {

    private String title;
    private String tier;
    private Long roomCreator;
    //최대 인원 100명
    private Integer userCountMax;

//    private LocalDateTime deletedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "chatRoom", fetch = FetchType.LAZY)
    private List<UserChatRoom> userChatRooms;

    @OneToMany(mappedBy = "chatRoom", fetch = FetchType.LAZY)
    private List<ChatMsg> chatMsgs;

    @OneToMany(mappedBy = "chatRoom", fetch = FetchType.LAZY)
    private List<Heart> hearts;

    private String password;

    private Boolean isPrivate;

    @Override
    public String toString() {
        return "ChatRoom{" +
                // BaseEntity에서 상속받은 id 사용
                "id=" + getId() +
                ", title='" + title + '\'' +
                ", tier='" + tier + '\'' +
                ", roomCreator=" + roomCreator +
                ", userCountMax=" + userCountMax +
                ", user=" + (user != null ? user.getId() : "null") +  // user가 null일 수 있기 때문에 체크
                ", userChatRooms=" + (userChatRooms != null ? userChatRooms.size() : 0) + // userChatRooms 리스트가 null일 수 있기 때문에 체크
                '}';
    }
    public void update(String title, LocalDateTime modifiedAt) {
        this.setTitle(title);
        this.setModifiedAt(modifiedAt);
    }
}