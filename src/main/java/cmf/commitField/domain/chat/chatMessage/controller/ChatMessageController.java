package cmf.commitField.domain.chat.chatMessage.controller;

import cmf.commitField.domain.chat.chatMessage.controller.request.ChatMsgRequest;
import cmf.commitField.domain.chat.chatMessage.controller.response.ChatMsgResponse;
import cmf.commitField.domain.chat.chatMessage.dto.ChatMsgDto;
import cmf.commitField.domain.chat.chatMessage.service.ChatMessageService;
import cmf.commitField.domain.user.entity.CustomOAuth2User;
import cmf.commitField.global.error.ErrorCode;
import cmf.commitField.global.globalDto.GlobalResponse;
import cmf.commitField.global.security.LoginCheck;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatMessageController {

    private final ChatMessageService chatMessageService;

    @PostMapping("/msg/{roomId}")
    @LoginCheck
    public GlobalResponse<Object> sendChat(
            @PathVariable Long roomId,
            @RequestBody @Valid ChatMsgRequest message) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof OAuth2AuthenticationToken)) {
            return GlobalResponse.error(ErrorCode.NOT_AUTHENTICATED);
        }

        CustomOAuth2User principal = (CustomOAuth2User) authentication.getPrincipal();
        Long userId = principal.getId(); // OAuth2 로그인한 유저의 ID 가져오기

        if (message == null || message.getMessage().trim().isEmpty()) {
            return GlobalResponse.error(ErrorCode.EMPTY_MESSAGE);
        }

        ChatMsgResponse response = chatMessageService.sendMessage(message, userId, roomId);
        return GlobalResponse.success("채팅 메시지 보내기 성공", response);
    }

    @GetMapping("/msg/{roomId}")
    @LoginCheck
    public GlobalResponse<Object> getChatList(
            @PathVariable Long roomId,
            @RequestParam(required = false) Long lastId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof OAuth2AuthenticationToken)) {
            return GlobalResponse.error(ErrorCode.NOT_AUTHENTICATED);
        }

        CustomOAuth2User principal = (CustomOAuth2User) authentication.getPrincipal();
        Long userId = principal.getId(); // OAuth2 로그인한 유저의 ID 가져오기

        List<ChatMsgDto> roomChatMsgList = chatMessageService.getRoomChatMsgList(roomId, userId, lastId);
        if (roomChatMsgList == null || roomChatMsgList.isEmpty()) {
            return GlobalResponse.error(ErrorCode.CHAT_NOT_FOUND);
        }

        return GlobalResponse.success("해당 채팅방의 메시지들을 조회하였습니다.", roomChatMsgList);
    }
}

