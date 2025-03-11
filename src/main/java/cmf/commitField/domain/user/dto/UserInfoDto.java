package cmf.commitField.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class UserInfoDto {

    private String username;
    private String nickname;
    private String email;
    private String avatarUrl;
    private LocalDateTime createdAt;
    private LocalDateTime lastCommitted;
    private long commitCount;
    private long seasonCommitCount;

    private int petType;
    private long petExp;
    private String petGrow;
    private String tier;

    // 펫 생략, 차후 필요시 추가
}
