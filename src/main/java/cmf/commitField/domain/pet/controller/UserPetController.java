package cmf.commitField.domain.pet.controller;


import cmf.commitField.domain.pet.entity.Pet;
import cmf.commitField.domain.pet.service.PetService;
import cmf.commitField.domain.pet.service.UserPetService;
import cmf.commitField.domain.user.entity.User;
import cmf.commitField.domain.user.service.CustomOAuth2UserService;
import cmf.commitField.global.globalDto.GlobalResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user-pets")
@RequiredArgsConstructor
public class UserPetController {

    private final UserPetService userPetService;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final PetService petService;

    // TODO: 기능 확장시 추가 예정
    // 유저의 도감 조회 (보유한 펫 목록)
    @GetMapping("/collection/{userId}")
    public GlobalResponse<List<Pet>> getUserPetCollection(@PathVariable Long userId) {
        User user = customOAuth2UserService.getUserById(userId).orElse(null);
        return GlobalResponse.success(userPetService.getUserPetCollection(user));
    }
}