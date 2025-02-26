package cmf.commitField.domain.chat.chatMessage.controller.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChatMsgRequest {
    @NotEmpty
    @Length(max = 300)
    private String message;

}