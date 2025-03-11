package cmf.commitField.domain.user.entity;

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
@Table(name = "tierRegacy")
public class TierRegacy extends BaseEntity {
    private String year;
    private String season;

    @Enumerated(EnumType.STRING)  // DB에 저장될 때 String 형태로 저장됨
    private Tier tier;

    public TierRegacy(User user){
        this.user=user;
        this.year="2025";
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
