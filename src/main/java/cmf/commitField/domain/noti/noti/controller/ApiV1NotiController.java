package cmf.commitField.domain.noti.noti.controller;

import cmf.commitField.domain.noti.noti.dto.NotiDto;
import cmf.commitField.domain.noti.noti.service.NotiService;
import cmf.commitField.domain.user.entity.User;
import cmf.commitField.domain.user.repository.UserRepository;
import cmf.commitField.domain.user.service.UserService;
import cmf.commitField.global.error.ErrorCode;
import cmf.commitField.global.exception.CustomException;
import cmf.commitField.global.globalDto.GlobalResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
    private final UserService userService;


    @GetMapping("")
    public GlobalResponse<List<NotiDto>> getNoti(@AuthenticationPrincipal OAuth2User oAuth2User) {
        String username = oAuth2User.getName();
        User user = userService.findByUsername(username);
        List<NotiDto> notis = notiService.getNotReadNoti(user);
        return GlobalResponse.success(notis);
    }

    @PostMapping("/read")
    public GlobalResponse<Object> readNoti(@AuthenticationPrincipal OAuth2User oAuth2User) {
        String username = oAuth2User.getName();
        User user = userService.findByUsername(username);
        notiService.read(user);
        return GlobalResponse.success("알림을 읽음 처리했습니다.");
    }
}