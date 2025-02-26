package cmf.commitField.domain.chat.chatRoom.service;

import cmf.commitField.domain.chat.chatMessage.repository.ChatMessageRepository;
import cmf.commitField.domain.chat.chatRoom.controller.request.ChatRoomRequest;
import cmf.commitField.domain.chat.chatRoom.dto.ChatRoomDto;
import cmf.commitField.domain.chat.chatRoom.entity.ChatRoom;
import cmf.commitField.domain.chat.chatRoom.repository.ChatRoomRepository;
import cmf.commitField.domain.chat.userChatRoom.entity.UserChatRoom;
import cmf.commitField.domain.chat.userChatRoom.repository.UserChatRoomRepository;
import cmf.commitField.domain.user.entity.User;
import cmf.commitField.domain.user.repository.UserRepository;
import cmf.commitField.global.error.ErrorCode;
import cmf.commitField.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final UserChatRoomRepository userChatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;

    @Override
    @Transactional
    public void createRoom(ChatRoomRequest chatRoomRequest, Long userId) {
        // 유저정보 조회
        User findUser = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        // findUser가 null이 아닐 경우, User의 ID를 사용
        if (findUser == null) {
            throw new CustomException(ErrorCode.NOT_FOUND_USER); // 예외 처리 추가
        }

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

        // 이미 참여한 방인지 확인
        if (userChatRoomRepository.existsByChatRoomIdAndUserId(roomId, userId)) {
            throw new CustomException(ErrorCode.ALREADY_JOIN_ROOM);
        }


        // user_chatroom 관계 생성
        UserChatRoom userChatRoom = UserChatRoom.builder()
                .user(findUser)
                .chatRoom(chatRoom)
                .build();
        userChatRoomRepository.save(userChatRoom);
    }

    // 방 조회 DTO 변환 메서드 추출
    private static List<ChatRoomDto> getChatRoomDtos(Page<ChatRoom> all) {
        List<ChatRoomDto> chatRoomList = new ArrayList<>();

        for (ChatRoom list : all) {
            ChatRoomDto dto = ChatRoomDto.builder()
                    .id(list.getId())
                    .title(list.getTitle())
                    .currentUserCount((long) list.getUserChatRooms().size())
                    .userCountMax(list.getUserCountMax())
                    .build();

            chatRoomList.add(dto);
        }
        return chatRoomList;
    }

    // 채팅방 전체 조회
    @Override
    @Transactional(readOnly = true)
    public List<ChatRoomDto> getRoomList(Pageable pageable) {
        Page<ChatRoom> all = chatRoomRepository.findAll(pageable);
        return getChatRoomDtos(all);
    }

    // 자신이 생성한 방 리스트 조회
    @Override
    @Transactional(readOnly = true)
    public List<ChatRoomDto> roomsByCreatorUser(Long userId, Pageable pageable) {
        Page<ChatRoom> all = chatRoomRepository.findAllByUserId(userId, pageable);
        return getChatRoomDtos(all);
    }

    // 자신이 참여한 방 리스트 조회
    @Override
    @Transactional(readOnly = true)
    public List<ChatRoomDto> getUserByRoomPartList(Long userId, Pageable pageable) {
        Page<ChatRoom> allByUserIdAndUserChatRooms = chatRoomRepository
                .findAllByUserChatRoomsUserId(userId, pageable);
        return getChatRoomDtos(allByUserIdAndUserChatRooms);
    }

    @Override
    @Transactional
    public void outRoom(Long userId, Long roomId) {
        ChatRoom room = chatRoomRepository
                .findChatRoomById(roomId)
                .orElseThrow(() -> new CustomException(ErrorCode.NO_ROOM_FOUND));
        // 방장이 아니라면
        if (!Objects.equals(room.getRoomCreator(), userId)) {
            userChatRoomRepository.deleteUserChatRoomByChatRoom_IdAndUserId(roomId, userId);
            return;
        }
        // 방장이라면 방 삭제
        chatMessageRepository.deleteChatMsgByChatRoom_Id(roomId); //방 삭제 시 채팅도 다 삭제(필요 시)
        userChatRoomRepository.deleteUserChatRoomByChatRoom_Id(roomId);
        chatRoomRepository.deleteById(roomId);

    }


    @Override
    @Transactional
    public void deleteRoom(Long userId, Long roomId) {
        ChatRoom room = chatRoomRepository
                .findChatRoomById(roomId)
                .orElseThrow(() -> new CustomException(ErrorCode.NO_ROOM));
        //방장이 아닐 경우, 삭제 불가
        if (!Objects.equals(room.getRoomCreator(), userId)) {
            throw new CustomException(ErrorCode.NOT_ROOM_CREATOR);
        }
        //모든 사용자 제거 후 방 삭제
        chatMessageRepository.deleteChatMsgByChatRoom_Id(roomId);
        userChatRoomRepository.deleteUserChatRoomByChatRoom_Id(roomId);
        chatRoomRepository.deleteById(roomId);

}



}
