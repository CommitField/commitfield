package cmf.commitField.domain.pet.dto;

import cmf.commitField.domain.pet.entity.UserPet;
import lombok.Getter;

@Getter
public class UserPetDto {
    private Long userId;
    private String username;
    private Long petId;
    private String petName;
    private boolean isHatched;

    public UserPetDto(UserPet userPet) {
        this.userId = userPet.getUser().getId();
        this.username = userPet.getUser().getUsername();
        this.petId = userPet.getPet().getId();
        this.petName = userPet.getPet().getName();
        this.isHatched = userPet.isHatched();
    }
}
