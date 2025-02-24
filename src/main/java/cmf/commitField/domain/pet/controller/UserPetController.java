package cmf.commitField.domain.pet.controller;


import cmf.commitField.domain.pet.dto.UserPetDto;
import cmf.commitField.domain.pet.entity.Pet;
import cmf.commitField.domain.pet.service.PetService;
import cmf.commitField.domain.pet.service.UserPetService;
import cmf.commitField.domain.user.entity.User;
import cmf.commitField.domain.user.service.CustomOAuth2UserService;
import cmf.commitField.global.globalDto.GlobalResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user-pets")
@RequiredArgsConstructor
public class UserPetController {

    private final UserPetService userPetService;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final PetService petService;

    // 유저가 펫을 부화
    @PostMapping("/hatch")
    public GlobalResponse<UserPetDto> hatchPet(@RequestParam Long userId, @RequestParam Long petId) {
        User user = customOAuth2UserService.getUserById(userId).orElse(null);
        Pet pet = petService.getPetById(petId);
        UserPetDto userPetDto = new UserPetDto(userPetService.hatchPet(user, pet));
        return GlobalResponse.success(userPetDto);
    }

    // 유저의 도감 조회 (보유한 펫 목록)
    @GetMapping("/collection/{userId}")
    public GlobalResponse<List<Pet>> getUserPetCollection(@PathVariable Long userId) {
        User user = customOAuth2UserService.getUserById(userId).orElse(null);
        return GlobalResponse.success(userPetService.getUserPetCollection(user));
    }
}