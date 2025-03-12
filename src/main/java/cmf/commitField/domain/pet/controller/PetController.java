package cmf.commitField.domain.pet.controller;

import cmf.commitField.domain.pet.dto.PetsDto;
import cmf.commitField.domain.pet.dto.UserPetDto;
import cmf.commitField.domain.pet.entity.Pet;
import cmf.commitField.domain.pet.service.PetService;
import cmf.commitField.domain.user.entity.CustomOAuth2User;
import cmf.commitField.domain.user.entity.User;
import cmf.commitField.domain.user.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/pets")
public class PetController {
    private final CustomOAuth2UserService userService;
    private final PetService petService;

    // 현재 펫 경험치 상승 및 상승 시 레벨업 처리
    @GetMapping("/exp")
    public ResponseEntity<UserPetDto> getPetExp(@AuthenticationPrincipal CustomOAuth2User oAuth2User){
        String username = oAuth2User.getName();

        System.out.println("/pet/exp, Username: "+username);
        UserPetDto userPetDto = petService.getExpPet(username, 0);
        return ResponseEntity.ok(userPetDto);
    }

    @GetMapping("/getall")
    public ResponseEntity<List<PetsDto>> getUserAllPets(@AuthenticationPrincipal CustomOAuth2User oAuth2User){
        String username = oAuth2User.getName();
        List<PetsDto> userPetDto = petService.getAllPets(username);
        return ResponseEntity.ok(userPetDto);
    }

    // *************************************
    // TODO : 아래는 확장 기능, 추가 시 개선 필요
    // *************************************

    // 새로운 펫 추가
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Pet createPet(
            @RequestParam String email,
            @RequestParam String name
    ) throws Exception {
        User user = userService.getUserByEmail(email).get();
        return petService.createPet(name, user);
    }

    // 모든 펫 조회
    @GetMapping
    public List<Pet> getAllPets() {
        return petService.getAllPets();
    }

    // 특정 펫 조회
    @GetMapping("/{petId}")
    public Pet getPetById(@PathVariable Long petId) {
        return petService.getPetById(petId);
    }

    // 펫 삭제
    @DeleteMapping("/{petId}")
    public void deletePet(@PathVariable Long petId) {
        petService.deletePet(petId);
    }

    public void getExpPet(User user, int commitCount){
        petService.getExpPet(user.getUsername(), commitCount);

    }
}
