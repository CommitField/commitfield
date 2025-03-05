package cmf.commitField.domain.noti.noti.controller;

import cmf.commitField.domain.noti.noti.dto.NotiDto;
import cmf.commitField.domain.noti.noti.service.NotiService;
import cmf.commitField.domain.user.entity.User;
import cmf.commitField.domain.user.repository.UserRepository;
import cmf.commitField.domain.user.service.CustomOAuth2UserService;
import cmf.commitField.global.error.ErrorCode;
import cmf.commitField.global.exception.CustomException;
import cmf.commitField.global.globalDto.GlobalResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Slf4j
public class ApiV1NotiController {
    private final NotiService notiService;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final UserRepository userRepository;

    @GetMapping("")
    public GlobalResponse<List<NotiDto>> getNoti() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof OAuth2AuthenticationToken) {
            OAuth2User principal = (OAuth2User) authentication.getPrincipal();
            Map<String, Object> attributes = principal.getAttributes();
            String username = (String) attributes.get("login");  // GitHub ID
            User user = userRepository.findByUsername(username).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
            List<NotiDto> notis = notiService.getNotReadNoti(user);
            return GlobalResponse.success(notis);
        }

        return GlobalResponse.error(ErrorCode.LOGIN_REQUIRED);
    }
}
