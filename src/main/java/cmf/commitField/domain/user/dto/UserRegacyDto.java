package cmf.commitField.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserRegacyDto {
    private long user_id;
    private String year;
    private String season;
    private String tier;
}
