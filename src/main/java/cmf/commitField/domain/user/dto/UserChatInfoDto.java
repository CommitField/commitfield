package cmf.commitField.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserChatInfoDto {
    private long id;
    private String username;
    private String nickname;
    private String email;
}
