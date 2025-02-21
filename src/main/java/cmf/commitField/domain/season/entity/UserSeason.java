package cmf.commitField.domain.season.entity;

import cmf.commitField.domain.mock.user.entity.MockUser;
import cmf.commitField.global.jpa.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_season")
public class UserSeason extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "user_id")
    private MockUser user;

    @ManyToOne
    @JoinColumn(name = "season_id")
    private Season season;

    @Enumerated(EnumType.STRING)
    @Column(name = "`rank`")  // 백틱(`)을 사용하여 예약어 문제 해결
    private Rank rank; // 예: SEED, TREE 등등
}
