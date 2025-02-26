package cmf.commitField.domain.pet.entity;

import cmf.commitField.domain.user.entity.User;
import cmf.commitField.global.jpa.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "pet")
public class Pet extends BaseEntity {
    private int type; // 펫 타입 넘버, 현재 1~3까지 존재
    private String name;
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
