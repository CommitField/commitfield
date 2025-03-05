package cmf.commitField.domain.pet.dto;

import cmf.commitField.domain.pet.entity.UserPet;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserPetDto {
    private String username;
    private Long petId;
    private String petName;
    private String grow;
    private long exp;

    public UserPetDto(UserPet userPet) {
        this.username = userPet.getUser().getUsername();
        this.petId = userPet.getPet().getId();
        this.petName = userPet.getPet().getName();
        this.isHatched = userPet.isHatched();
    }
}
