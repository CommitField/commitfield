package cmf.commitField.domain.chat.chatRoom.service;

import cmf.commitField.domain.chat.chatRoom.controller.request.ChatRoomRequest;
import cmf.commitField.domain.chat.chatRoom.controller.request.ChatRoomUpdateRequest;
import cmf.commitField.domain.chat.chatRoom.dto.ChatRoomDto;
import cmf.commitField.domain.chat.chatRoom.dto.ChatRoomUserDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ChatRoomService {

    void createRoom(ChatRoomRequest chatRoomRequest, Long userId);  // userId를 받도록 수정

    void joinRoom(Long roomId, Long userId);  // userId를 받도록 수정

    // 채팅방 전체 조회
    List<ChatRoomDto> getRoomList(Pageable pageable);

    // 자신이 생성한 방 리스트 조회
    List<ChatRoomDto> roomsByCreatorUser(Long userId, Pageable pageable);

    List<ChatRoomDto> getUserByRoomPartList(Long userId, Pageable pageable);

    void outRoom(Long userId, Long roomId);

    void deleteRoom(Long userId, Long roomId);
    //채팅방 제목 수정
    void updateRoom(Long roomId, ChatRoomUpdateRequest chatRoomUpdateRequest, Long userId);

    List<ChatRoomUserDto> getRoomUsers(Long roomId, Long userId);

    List<ChatRoomDto> getRoomHeartSortList(Pageable pageable);

    List<ChatRoomDto> myHeartRoomList(Long userId, Pageable pageable);

}
