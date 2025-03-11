package cmf.commitField.domain.chat.chatRoom.controller.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomRequest {

    @NotNull
    @Length(min = 2, max = 20)
    private String title;

    @NotNull
    @Max(100)
    private Integer userCountMax;

    @Length(min = 4, max = 20)
    private String password;

    private MultipartFile file;  // 파일을 받기 위한 필드

//    public boolean isFileSizeValid() {
//        return file == null || file.getSize() <= 5 * 1024 * 1024;  // 5MB
//    }

}
