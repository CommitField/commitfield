package cmf.commitField.domain.pet.controller;


import cmf.commitField.domain.pet.dto.UserPetListDto;
import cmf.commitField.domain.pet.service.PetService;
import cmf.commitField.domain.pet.service.UserPetService;
import cmf.commitField.domain.user.entity.CustomOAuth2User;
import cmf.commitField.domain.user.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user-pets")
@RequiredArgsConstructor
public class UserPetController {

    private final UserPetService userPetService;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final PetService petService;

    // 유저의 도감 조회 (보유한 펫 목록)
    @GetMapping("/collection")
    public ResponseEntity<UserPetListDto> getUserPetCollection(@AuthenticationPrincipal CustomOAuth2User oAuth2User) {
        String username = oAuth2User.getName();
        UserPetListDto userPetListDto = petService.getAllPets(username);
        return ResponseEntity.ok(userPetListDto);
    }
}