package cmf.commitField.domain.chat.chatRoom.controller.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomRequest {

    @NotEmpty
    @Length(min = 2, max = 20)
    private String title;

    @NotNull
    @Max(100)
    private Integer userCountMax;


}
