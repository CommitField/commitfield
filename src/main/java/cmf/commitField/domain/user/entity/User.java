package cmf.commitField.domain.user.entity;

import cmf.commitField.global.jpa.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
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
}
