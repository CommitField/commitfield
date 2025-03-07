package cmf.commitField.domain.chat.chatRoom.controller;

import cmf.commitField.domain.File.service.FileService;
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
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatRoomController {
    private final ChatRoomService chatRoomService;
    private final FileService fileService;

    // 채팅방 생성 (파일 업로드 포함)
    @PostMapping(value = "/room", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public GlobalResponse<Object> createRoom(
            @ModelAttribute @Valid ChatRoomRequest chatRoomRequest) throws IOException {


        // 인증 확인
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof OAuth2AuthenticationToken) {
            CustomOAuth2User principal = (CustomOAuth2User) authentication.getPrincipal();
            Long userId = principal.getId();  // getId()를 통해 userId를 추출

            // 파일 업로드 처리
            String imageUrl = null;
            if (chatRoomRequest.getFile() != null && !chatRoomRequest.getFile().isEmpty()) {
                imageUrl = fileService.saveFile(chatRoomRequest.getFile());  // 파일 저장
            }

            // 채팅방 생성 서비스 호출 (이미지 URL 포함)
            chatRoomService.createRoom(chatRoomRequest, userId, imageUrl);

            return GlobalResponse.success("채팅방을 생성하였습니다.");
        } else {
            throw new IllegalArgumentException("로그인 후에 이용해 주세요.");
        }
    }

    //채팅방 입장
    @PostMapping("/room/join/{roomId}")
    public GlobalResponse<Object> joinRoom(@PathVariable Long roomId, @RequestBody ChatRoomRequest chatRoomRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof OAuth2AuthenticationToken) {
            CustomOAuth2User principal = (CustomOAuth2User) authentication.getPrincipal();
            Long userId = principal.getId();  // getId()를 통해 userId를 추출
            chatRoomService.joinRoom(roomId, userId, chatRoomRequest);  // userId를 전달
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

    //채팅방 제목 수정
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

    // 전체 리스트에서 좋아요 순으로 정렬
    @GetMapping("/room/heart")
    @LoginCheck
    public GlobalResponse<Object> roomHeartSort(Pageable pageable) {
        List<ChatRoomDto> roomList = chatRoomService.getRoomHeartSortList(pageable);
        if (roomList.isEmpty()) {
            return GlobalResponse.error(ErrorCode.NO_ROOM);
        } else {
            return GlobalResponse.success(roomList);
        }
    }

    // 사용자(자신)가 좋아요 누른 방 리스트 조회
    @GetMapping("/room/myHeart/list")
    @LoginCheck
    public GlobalResponse<Object> getMyHeartRoomList(
            Pageable pageable,
            @AuthenticationPrincipal CustomOAuth2User principal) { // 인증된 사용자 정보 주입

        Long userId = principal.getId(); // 현재 로그인된 사용자 ID 가져오기
        List<ChatRoomDto> list = chatRoomService.myHeartRoomList(userId, pageable);

        if (list.isEmpty()) {
            return GlobalResponse.error(ErrorCode.NO_ROOM_FOUND);
        }
        return GlobalResponse.success("좋아요 누른 채팅방 리스트 조회 완료", list);
    }

    // 채팅방 제목 검색 조회
    @GetMapping("/room/search")
    @LoginCheck
    public GlobalResponse<Object> searchRoomName(
            @RequestParam(name = "roomName") String roomName,
            Pageable pageable) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof OAuth2AuthenticationToken) {
            CustomOAuth2User principal = (CustomOAuth2User) authentication.getPrincipal();
            Long userId = principal.getId();  // Extract userId from the principal

            if (roomName.isEmpty()) {
                throw new IllegalArgumentException("원하는 채팅방의 제목을 입력하세요.");
            }

            List<ChatRoomDto> list = chatRoomService.searchRoomByTitle(roomName, userId, pageable);
            return GlobalResponse.success(list);
        } else {
            throw new IllegalArgumentException("로그인 후에 이용해 주세요.");
        }
    }





}
