package cmf.commitField.domain.mock.user.entity;

import cmf.commitField.global.jpa.BaseEntity;
import com.fasterxml.jackson.databind.ser.Serializers;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "mock_user")
public class MockUser extends BaseEntity {
    private String email;
    private String nickname;
}
