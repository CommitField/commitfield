package cmf.commitField.domain.pet.entity;

import cmf.commitField.domain.user.entity.User;
import cmf.commitField.global.jpa.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Random;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "pet")
public class Pet extends BaseEntity {
    private int type; // 펫 타입 넘버, 현재 0~2까지 존재
    private String name;
    private String imageUrl;
    private int exp; // 펫 경험치

    @Enumerated(EnumType.STRING)  // DB에 저장될 때 String 형태로 저장됨
    private Grow grow; // 성장 정도

    public Pet(String name, User user){
        Random random = new Random();
        this.type = random.nextInt(3);
        switch (type){ //정해진 알 타입에 따라 다른 url을 주입
            case 0: this.imageUrl = "temp0";
                 break;
            case 1: this.imageUrl = "temp1";
                break;
            case 2: this.imageUrl = "temp2";
                break;
        }
        this.name = name;
        this.exp = 0;
        this.grow = Grow.EGG;
        this.user = user;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public enum Grow {
        EGG, HATCH, GROWN
    }

    public int addExp(int commit){
        exp+=commit;

        return exp;
    }
}
