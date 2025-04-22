package cmf.commitField.domain.admin.notice.entity;


import cmf.commitField.global.jpa.BaseEntity;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@SuperBuilder
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Notice extends BaseEntity {
    // 관리자 공지사항
    private String title;
    private String content;
}
