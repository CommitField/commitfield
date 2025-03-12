package cmf.commitField.domain.user.controller;

import cmf.commitField.domain.commit.totalCommit.service.TotalCommitService;
import cmf.commitField.domain.user.dto.UserChatInfoDto;
import cmf.commitField.domain.user.dto.UserInfoDto;
import cmf.commitField.domain.user.dto.UserRegacyDto;
import cmf.commitField.domain.user.entity.CustomOAuth2User;
import cmf.commitField.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final TotalCommitService totalCommitService;

    @GetMapping("/chatinfo")
    public ResponseEntity<UserChatInfoDto> getUserChatInfo(@AuthenticationPrincipal CustomOAuth2User oAuth2User){
        String username = oAuth2User.getName();

        //유저 정보의 조회
        //이 이벤트가 일어나면 유저의 정보를 확인하고, 조회한 유저를 active 상태로 만든다.
        UserChatInfoDto userChatInfoDto = userService.showUserChatInfo(username);

        return ResponseEntity.ok(userChatInfoDto);
    }

    @GetMapping("info")
    public ResponseEntity<UserInfoDto> getUserInfo(@AuthenticationPrincipal CustomOAuth2User oAuth2User){
        String username = oAuth2User.getName();

        //유저 정보의 조회
        //이 이벤트가 일어나면 유저의 정보를 확인하고, 조회한 유저를 active 상태로 만든다.
        UserInfoDto userInfoDto = userService.showUserInfo(username);

        return ResponseEntity.ok(userInfoDto);
    }

    @GetMapping("/tierinfo")
    public ResponseEntity<List<UserRegacyDto>> getUserTierInfo(@AuthenticationPrincipal CustomOAuth2User oAuth2User) {
        String username = oAuth2User.getName();
        List<UserRegacyDto> tierList = userService.showUserRegacy(username);
        return ResponseEntity.ok(tierList);
    }
}
