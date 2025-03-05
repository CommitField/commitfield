package cmf.commitField.domain.commit.scheduler;

import cmf.commitField.domain.user.dto.UserInfoDto;
import cmf.commitField.domain.user.entity.CustomOAuth2User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/commits")
public class CommitUpdateController {
    private final CommitUpdateService commitUpdateService;
    @GetMapping("/tier")
    public ResponseEntity<UserInfoDto> getUserTier(@AuthenticationPrincipal CustomOAuth2User oAuth2User){
        String username = oAuth2User.getName();  // CustomOAuth2User의 getName()은 user.getUsername()을 반환

        System.out.println("/tier, Username: "+username);
        UserInfoDto userInfoDto = commitUpdateService.updateUserTier(username);
        return ResponseEntity.ok(userInfoDto);
    }

}
