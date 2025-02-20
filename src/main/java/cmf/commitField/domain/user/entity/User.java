package cmf.commitField.domain.user.entity;

import cmf.commitField.global.jpa.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Builder;

@Entity
@Builder
public class User extends BaseEntity {
    @Id
    private long id;
    private String email;
    private String nickname;
    private String password;
}
