package cmf.commitField.domain.pet.controller;

import cmf.commitField.domain.pet.entity.Pet;
import cmf.commitField.domain.pet.service.PetService;
import cmf.commitField.domain.user.entity.User;
import cmf.commitField.domain.user.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/pets")
public class PetController {
    private final CustomOAuth2UserService userService;
    private final PetService petService;

    // 새로운 펫 추가
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Pet createPet(
            @RequestParam String email,
            @RequestParam String name,
            @RequestPart(value = "imageFile") MultipartFile imageFile
    ) throws Exception {
        User user = userService.getUserByEmail(email).get();
        return petService.createPet(name, imageFile, user);
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
        petService.getExpPet(user, commitCount);

    }
}
