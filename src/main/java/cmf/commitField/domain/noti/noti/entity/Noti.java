package cmf.commitField.domain.noti.noti.entity;

import cmf.commitField.domain.user.entity.User;
import cmf.commitField.global.jpa.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;

import static lombok.AccessLevel.PROTECTED;

@Entity
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PROTECTED)
@SuperBuilder
@Getter
@Setter
public class Noti extends BaseEntity {
    @Enumerated(EnumType.STRING)
    private NotiType typeCode; // 알림 타입
    private NotiDetailType type2Code; // 알림 세부 타입
    @ManyToOne
    private User receiver; // 알림을 받는 사람
    @ColumnDefault("false")
    private boolean isRead; // 읽음 상태
    private String message; // 알림 메시지

    // TODO: 알림이 연결된 객체 어떻게 처리할지 고민 필요.
//    private String relTypeCode; // 알림이 연결된 실제 객체 유형
//    private long relId; // 알림 객체의 Id
}
