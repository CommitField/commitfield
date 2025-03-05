package cmf.commitField.domain.pet.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserPetDto {
    private String username;
    private long petId;
    private String petName;
    private String grow;
    private long exp;
}
