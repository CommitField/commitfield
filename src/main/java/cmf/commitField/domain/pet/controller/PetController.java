package cmf.commitField.domain.pet.controller;

import cmf.commitField.domain.pet.dto.PetsDto;
import cmf.commitField.domain.pet.dto.UserPetDto;
import cmf.commitField.domain.pet.entity.Pet;
import cmf.commitField.domain.pet.service.PetService;
import cmf.commitField.domain.user.entity.CustomOAuth2User;
import cmf.commitField.domain.user.entity.User;
import cmf.commitField.domain.user.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
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
    @PostMapping("/new")
    public ResponseEntity<Pet> createPet(@AuthenticationPrincipal CustomOAuth2User oAuth2User) throws Exception {
        String username = oAuth2User.getName();
        Pet pet = petService.createPet("알알", username);
        if(pet == null){
            //사용자가 현재 펫이 GROWN 상태가 아닌데도 다른 경로를 통해 요청한 경우
            ResponseEntity.badRequest();
        }
        return ResponseEntity.ok(pet);
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
