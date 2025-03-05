package cmf.commitField.domain.heart.controller;

import cmf.commitField.domain.heart.service.HeartService;
import cmf.commitField.domain.user.entity.CustomOAuth2User;
import cmf.commitField.global.globalDto.GlobalResponse;
import cmf.commitField.global.security.LoginCheck;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/heart")
public class HeartController {

    private final HeartService heartService;

    @PostMapping("/{roomId}")
    @LoginCheck
    public GlobalResponse<Object> heart(@PathVariable Long roomId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof OAuth2AuthenticationToken) {
            CustomOAuth2User principal = (CustomOAuth2User) authentication.getPrincipal();
            Long userId = principal.getId(); // OAuth2 사용자 ID 추출
            heartService.heart(userId, roomId);
            return GlobalResponse.success("좋아요를 눌렀습니다.");
        } else {
            throw new IllegalArgumentException("로그인 후에 이용해 주세요.");
        }
    }

    @DeleteMapping("/{roomId}")
    @LoginCheck
    public GlobalResponse<Object> heartDelete(@PathVariable Long roomId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof OAuth2AuthenticationToken) {
            CustomOAuth2User principal = (CustomOAuth2User) authentication.getPrincipal();
            Long userId = principal.getId(); // OAuth2 사용자 ID 추출
            heartService.heartDelete(userId, roomId);
            return GlobalResponse.success("좋아요를 취소했습니다.");
        } else {
            throw new IllegalArgumentException("로그인 후에 이용해 주세요.");
        }
    }
}
