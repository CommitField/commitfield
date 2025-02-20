package cmf.commitField.domain.user.entity;

import cmf.commitField.global.jpa.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
class User extends BaseEntity {
    private String email;
    private String nickname;
    private String password;
}
