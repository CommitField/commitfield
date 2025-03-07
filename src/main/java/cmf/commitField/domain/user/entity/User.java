package cmf.commitField.domain.user.entity;

import cmf.commitField.domain.chat.chatMessage.entity.ChatMsg;
import cmf.commitField.domain.chat.chatRoom.entity.ChatRoom;
import cmf.commitField.domain.chat.userChatRoom.entity.UserChatRoom;
import cmf.commitField.domain.heart.entity.Heart;
import cmf.commitField.domain.pet.entity.Pet;
import cmf.commitField.global.jpa.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private Boolean status; //로그인 true, 로그아웃 false
    private LocalDateTime lastCommitted; // 마지막 커밋 시간
    private long commitCount; // 전체
    private long seasonCommitCount; // 이번 시즌

    @Enumerated(EnumType.STRING)  // 권한
    private Role role;

    public enum Role {
        USER, ADMIN
    }

    @Enumerated(EnumType.STRING)  // DB에 저장될 때 String 형태로 저장됨
    private Tier tier;

    public enum Tier {
        SEED(0),      // 씨앗
        SPROUT(95),    // 새싹
        FLOWER(189),    // 꽃
        FRUIT(283),     // 열매
        TREE(377);       // 나무

        private final long requiredExp;

        Tier(long requiredExp) {
            this.requiredExp = requiredExp;
        }

        public long getRequiredExp() {
            return requiredExp;
        }

        // 현재 경험치에 맞는 레벨 찾기
        public static Tier getLevelByExp(long exp) {
            Tier currentLevel = SEED;
            for (Tier level : values()) {
                if (exp >= level.getRequiredExp()) {
                    currentLevel = level;
                }
            }
            return currentLevel;
        }
    }


    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JsonIgnore
    private List<ChatRoom> chatRooms = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JsonIgnore
    private List<UserChatRoom> userChatRooms = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JsonIgnore
    private List<ChatMsg> chatMsgs = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JsonIgnore
    private List<Pet> pets = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Heart> hearts;

    public User(String username, String email, String nickname, String avatarUrl, Boolean status, List<ChatRoom> cr, List<UserChatRoom> ucr, List<ChatMsg> cmsg){
        this.username=username;
        this.email=email;
        this.nickname=nickname;
        this.avatarUrl=avatarUrl;
        this.status= status;
        this.role = Role.USER;
        this.tier = Tier.SEED;
        this.chatRooms = cr;
        this.userChatRooms = ucr;
        this.chatMsgs = cmsg;
        this.lastCommitted = LocalDateTime.now();
        this.commitCount = 0;
        this.seasonCommitCount = 0;
    }

    public void addPets(Pet pet){
        pets.add(pet);
    }

    public void addExp(long commitCount){
        this.seasonCommitCount+=commitCount;
    }

    public void addCommitCount(long commitCount){
        this.commitCount+=commitCount;
    }
}
