package cmf.commitField.domain.chat.chatRoom.service;

import cmf.commitField.domain.chat.chatRoom.controller.request.ChatRoomRequest;
import cmf.commitField.domain.chat.chatRoom.entity.ChatRoom;
import cmf.commitField.domain.chat.chatRoom.repository.ChatRoomRepository;
import cmf.commitField.domain.chat.userChatRoom.entity.UserChatRoom;
import cmf.commitField.domain.chat.userChatRoom.repository.UserChatRoomRepository;
import cmf.commitField.domain.user.entity.User;
import cmf.commitField.domain.user.repository.UserRepository;
import cmf.commitField.global.error.ErrorCode;
import cmf.commitField.global.exception.CustomException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final UserChatRoomRepository userChatRoomRepository;

    @Override
    @Transactional
    public void createRoom(ChatRoomRequest chatRoomRequest, Long userId) {
        // 유저정보 조회
        User findUser = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        // ChatRoom 생성
        ChatRoom chatRoom = ChatRoom.builder()
                .roomCreator(findUser.getId())
                .title(chatRoomRequest.getTitle())
                .userCountMax(chatRoomRequest.getUserCountMax())
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build();
        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);

        // 연관관계 user_chat room 생성
        UserChatRoom userChatRoom = UserChatRoom.builder()
                .user(findUser)
                .chatRoom(savedChatRoom)
                .build();
        userChatRoomRepository.save(userChatRoom);
    }

    @Override
    @Transactional
    public void joinRoom(Long roomId, Long userId) {
        // 유저 조회
        User findUser = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        // room 조회
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ROOM));

        // 현재 인원 수 조회
        Long currentUserCount = userChatRoomRepository.countByChatRoomId(roomId);

        // 채팅방이 꽉 찼을 경우 예외 처리
        if (currentUserCount >= chatRoom.getUserCountMax()) {
            throw new CustomException(ErrorCode.ROOM_USER_FULL);
        }

        // user_chatroom 관계 생성
        UserChatRoom userChatRoom = UserChatRoom.builder()
                .user(findUser)
                .chatRoom(chatRoom)
                .build();
        userChatRoomRepository.save(userChatRoom);
    }
}
