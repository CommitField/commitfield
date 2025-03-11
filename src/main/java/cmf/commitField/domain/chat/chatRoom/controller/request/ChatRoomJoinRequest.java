package cmf.commitField.domain.chat.chatRoom.controller.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class ChatRoomJoinRequest {
    @NotNull
    @Length(min = 2, max = 20)
    private String title;

    @NotNull
    @Max(100)
    private Integer userCountMax;

    @Length(min = 4, max = 20)
    private String password;
}
