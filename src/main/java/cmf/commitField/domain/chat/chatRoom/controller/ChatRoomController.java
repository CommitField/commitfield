package cmf.commitField.domain.chat.chatRoom.controller;

import cmf.commitField.domain.chat.chatRoom.controller.request.ChatRoomRequest;
import cmf.commitField.domain.chat.chatRoom.dto.ChatRoomDto;
import cmf.commitField.domain.chat.chatRoom.service.ChatRoomService;
import cmf.commitField.domain.user.entity.CustomOAuth2User;
import cmf.commitField.global.globalDto.GlobalResponse;
import cmf.commitField.global.security.LoginCheck;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatRoomController {
    private final ChatRoomService chatRoomService;

    //채팅방 생성
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

    //채팅방 입장
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

    // 전체 리스트
    @GetMapping("/room")
    @LoginCheck
    public ResponseEntity<Object> roomList(Pageable pageable) {
        List<ChatRoomDto> roomList = chatRoomService.getRoomList(pageable);
        return ResponseEntity.ok().body(roomList);
    }

    // 사용자(자신)가 생성한 방 리스트 조회
    @GetMapping("/room/creator")
    @LoginCheck
    public ResponseEntity<Object> getByUserRoomList(Pageable pageable) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof OAuth2AuthenticationToken) {
            CustomOAuth2User principal = (CustomOAuth2User) authentication.getPrincipal();
            Long userId = principal.getId();  // getId()를 통해 userId를 추출

            List<ChatRoomDto> userByRoomList = chatRoomService.getUserByRoomList(userId, pageable);
            return ResponseEntity.ok().body(userByRoomList);
        } else {
            throw new IllegalArgumentException("User not logged in.");
        }
    }

    // 사용자(자신)가 들어가 있는 방 리스트 조회
    @GetMapping("/room/part")
    @LoginCheck
    public ResponseEntity<Object> getByUserRoomPartList(Pageable pageable) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof OAuth2AuthenticationToken) {
            CustomOAuth2User principal = (CustomOAuth2User) authentication.getPrincipal();
            Long userId = principal.getId();  // getId()를 통해 userId를 추출
            List<ChatRoomDto> userByRoomPartList = chatRoomService.getUserByRoomPartList(userId, pageable);
            return ResponseEntity.ok().body(userByRoomPartList);
        } else {
            throw new IllegalArgumentException("User not logged in.");
        }
    }

//    // 채팅방 나가기
//    @DeleteMapping("/room/out/{roomId}")
//    @LoginCheck
//    public ResponseEntity<Object> outRoom(
//            @PathVariable Long roomId) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        if (authentication instanceof OAuth2AuthenticationToken) {
//            CustomOAuth2User principal = (CustomOAuth2User) authentication.getPrincipal();
//            Long userId = principal.getId();  // getId()를 통해 userId를 추출
//            chatRoomService.outRoom(userId, roomId);
//            return ResponseEntity.ok().body("success");
//        } else {
//            throw new IllegalArgumentException("User not logged in.");
//        }
//    }
//
//    // 채팅방 삭제
//    @DeleteMapping("/room/delete/{roomId}")
//    @LoginCheck
//    public ResponseEntity<Object> deleteRoom(
//            @PathVariable Long roomId) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        if (authentication instanceof OAuth2AuthenticationToken) {
//            CustomOAuth2User principal = (CustomOAuth2User) authentication.getPrincipal();
//            Long userId = principal.getId();  // getId()를 통해 userId를 추출
//            chatRoomService.deleteRoom(userId, roomId);
//            return ResponseEntity.ok().body("success");
//        } else {
//            throw new IllegalArgumentException("User not logged in.");
//        }
//    }

}
