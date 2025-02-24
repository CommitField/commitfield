package cmf.commitField.domain.chat.chatRoom.service;

import cmf.commitField.domain.chat.chatRoom.controller.request.ChatRoomRequest;

public interface ChatRoomService {

    void createRoom(ChatRoomRequest chatRoomRequest, Long userId);  // userId를 받도록 수정

    void joinRoom(Long roomId, Long userId);  // userId를 받도록 수정
}
