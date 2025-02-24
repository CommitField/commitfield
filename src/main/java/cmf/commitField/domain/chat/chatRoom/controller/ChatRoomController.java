package cmf.commitField.domain.chat.chatRoom.controller;

import cmf.commitField.domain.chat.chatRoom.controller.request.ChatRoomRequest;
import cmf.commitField.domain.chat.chatRoom.service.ChatRoomService;
import cmf.commitField.domain.user.entity.CustomOAuth2User;
import cmf.commitField.global.globalDto.GlobalResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatRoomController {
    private final ChatRoomService chatRoomService;

    @PostMapping("/room")
    public GlobalResponse<Object> createRoom(
            @RequestBody @Valid ChatRoomRequest chatRoomRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof OAuth2AuthenticationToken) {
            CustomOAuth2User principal = (CustomOAuth2User) authentication.getPrincipal();
            Long userId = principal.getId();  // getId()를 통해 userId를 추출
            chatRoomService.createRoom(chatRoomRequest, userId);  // userId를 전달
            return GlobalResponse.success();
        } else {
            throw new IllegalArgumentException("User not logged in.");
        }
    }

    @PostMapping("/room/join/{roomId}")
    public GlobalResponse<Object> joinRoom(@PathVariable Long roomId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof OAuth2AuthenticationToken) {
            CustomOAuth2User principal = (CustomOAuth2User) authentication.getPrincipal();
            Long userId = principal.getId();  // getId()를 통해 userId를 추출
            chatRoomService.joinRoom(roomId, userId);  // userId를 전달
            return GlobalResponse.success();
        } else {
            throw new IllegalArgumentException("User not logged in.");
        }
    }
}
