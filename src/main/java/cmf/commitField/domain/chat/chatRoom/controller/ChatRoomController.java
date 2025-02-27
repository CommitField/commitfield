package cmf.commitField.domain.chat.chatRoom.controller;

import cmf.commitField.domain.chat.chatRoom.controller.request.ChatRoomRequest;
import cmf.commitField.domain.chat.chatRoom.controller.request.ChatRoomUpdateRequest;
import cmf.commitField.domain.chat.chatRoom.dto.ChatRoomDto;
import cmf.commitField.domain.chat.chatRoom.dto.ChatRoomUserDto;
import cmf.commitField.domain.chat.chatRoom.service.ChatRoomService;
import cmf.commitField.domain.user.entity.CustomOAuth2User;
import cmf.commitField.global.error.ErrorCode;
import cmf.commitField.global.globalDto.GlobalResponse;
import cmf.commitField.global.security.LoginCheck;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
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
            return GlobalResponse.success("채팅방을 생성하였습니다.");
        } else {
            throw new IllegalArgumentException("로그인 후에 이용해 주세요.");
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
            return GlobalResponse.success("해당 채팅방에 입장하셨습니다");
        } else {
            throw new IllegalArgumentException("로그인 후에 이용해 주세요.");
        }
    }

    // 전체 리스트
    @GetMapping("/room")
    @LoginCheck
    public GlobalResponse<Object> roomList(Pageable pageable) {
        List<ChatRoomDto> roomList = chatRoomService.getRoomList(pageable);

        // 방 리스트가 비어 있으면 notFound 응답 반환
        if (roomList.isEmpty()) {
            return GlobalResponse.error(ErrorCode.NO_ROOM_FOUND);
        }

        return GlobalResponse.success("전체 목록 조회에 성공하였습니다.",roomList);
    }

    // 사용자(자신)가 생성한 방 리스트 조회
    @GetMapping("/room/creator")
    @LoginCheck
    public GlobalResponse<Object> getByUserRoomList(Pageable pageable) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof OAuth2AuthenticationToken) {
            CustomOAuth2User principal = (CustomOAuth2User) authentication.getPrincipal();
            Long userId = principal.getId();  // getId()를 통해 userId를 추출

            List<ChatRoomDto> userByRoomList = chatRoomService.roomsByCreatorUser(userId, pageable);

            // 방 리스트가 비어 있으면 notFound 응답 반환
            if (userByRoomList.isEmpty()) {
                return GlobalResponse.error(ErrorCode.USER_CREATED_ROOM_NOT_FOUND);
            }

            return GlobalResponse.success("사용자가 생성한 방 조회 성공.",userByRoomList);
        } else {
            throw new IllegalArgumentException("로그인 후에 이용해 주세요.");
        }
    }

    // 사용자(자신)가 들어가 있는 방 리스트 조회
    @GetMapping("/room/part")
    @LoginCheck
    public GlobalResponse<Object> getByUserRoomPartList(Pageable pageable) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof OAuth2AuthenticationToken) {
            CustomOAuth2User principal = (CustomOAuth2User) authentication.getPrincipal();
            Long userId = principal.getId();  // getId()를 통해 userId를 추출
            List<ChatRoomDto> userByRoomPartList = chatRoomService.getUserByRoomPartList(userId, pageable);

            // 만약 방 리스트가 없다면 notFound 응답 반환
            if (userByRoomPartList.isEmpty()) {
                return GlobalResponse.error(ErrorCode.NONE_ROOM);
            }

            return GlobalResponse.success("사용자가 들어가 있는 방 리스트 조회 성공.",userByRoomPartList);
        } else {
            throw new IllegalArgumentException("로그인 후에 이용해 주세요.");
        }
    }

    @PutMapping("/room/update/{roomId}")
    @LoginCheck
    public GlobalResponse<Object> updateRoom(
            @PathVariable Long roomId,
            @RequestBody @Valid ChatRoomUpdateRequest chatRoomUpdateRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof OAuth2AuthenticationToken) {
            CustomOAuth2User principal = (CustomOAuth2User) authentication.getPrincipal();
            Long userId = principal.getId();  // getId()를 통해 userId를 추출
            chatRoomService.updateRoom(roomId, chatRoomUpdateRequest, userId);  // userId를 전달
            return GlobalResponse.success("채팅방을 업데이트 했습니다.");
        } else {
            throw new IllegalArgumentException("로그인 후에 이용해 주세요.");
        }
    }

    // 채팅방 나가기
    @DeleteMapping("/room/out/{roomId}")
    @LoginCheck
    public GlobalResponse<Object> outRoom(
            @PathVariable Long roomId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof OAuth2AuthenticationToken) {
            CustomOAuth2User principal = (CustomOAuth2User) authentication.getPrincipal();
            Long userId = principal.getId();  // getId()를 통해 userId를 추출
            chatRoomService.outRoom(userId, roomId);
            return GlobalResponse.success("채팅방을 나갔습니다.");
        } else {
            throw new IllegalArgumentException("로그인 후에 이용해 주세요.");
        }
    }

    // 채팅방 삭제
    @DeleteMapping("/room/delete/{roomId}")
    @LoginCheck
    public GlobalResponse<Object> deleteRoom(
            @PathVariable Long roomId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof OAuth2AuthenticationToken) {
            CustomOAuth2User principal = (CustomOAuth2User) authentication.getPrincipal();
            Long userId = principal.getId();  // getId()를 통해 userId를 추출
            chatRoomService.deleteRoom(userId, roomId);
            return GlobalResponse.success("채팅방을 삭제했습니다.");
        } else {
            throw new IllegalArgumentException("로그인 후에 이용해 주세요.");
        }
    }

    //채팅방 유저 목록 조회
    @GetMapping("/room/users/{roomId}")
    @LoginCheck
    public GlobalResponse<Object> getRoomUsers(
            @PathVariable Long roomId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof OAuth2AuthenticationToken) {
            CustomOAuth2User principal = (CustomOAuth2User) authentication.getPrincipal();
            Long userId = principal.getId();  // Extract userId from the principal
            List<ChatRoomUserDto> roomUsers = chatRoomService.getRoomUsers(roomId, userId);
            return GlobalResponse.success(roomUsers);
        } else {
            throw new IllegalArgumentException("로그인 후에 이용해 주세요.");
        }
    }



}
