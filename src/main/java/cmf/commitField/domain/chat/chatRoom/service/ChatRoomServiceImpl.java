package cmf.commitField.domain.chat.chatRoom.service;

import cmf.commitField.domain.chat.chatMessage.repository.ChatMessageRepository;
import cmf.commitField.domain.chat.chatRoom.controller.request.ChatRoomRequest;
import cmf.commitField.domain.chat.chatRoom.controller.request.ChatRoomUpdateRequest;
import cmf.commitField.domain.chat.chatRoom.dto.ChatRoomDto;
import cmf.commitField.domain.chat.chatRoom.dto.ChatRoomUserDto;
import cmf.commitField.domain.chat.chatRoom.entity.ChatRoom;
import cmf.commitField.domain.chat.chatRoom.repository.ChatRoomRepository;
import cmf.commitField.domain.chat.userChatRoom.entity.UserChatRoom;
import cmf.commitField.domain.chat.userChatRoom.repository.UserChatRoomRepository;
import cmf.commitField.domain.user.entity.User;
import cmf.commitField.domain.user.repository.UserRepository;
import cmf.commitField.global.error.ErrorCode;
import cmf.commitField.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    private final ChatMessageRepository chatMessageRepository;

    private final UserRepository userRepository;

    private final UserChatRoomRepository userChatRoomRepository;

    private final RedissonClient redissonClient;

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

    // 방 조회 DTO 변환 메서드 추출
    private static List<ChatRoomDto> getChatRoomDtos(Page<ChatRoom> all) {
        return all.stream()
                .map(ChatRoomDto::of)
                .collect(Collectors.toList());
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
        Page<ChatRoom> userCreateAll = chatRoomRepository.findAllByUserId(userId, pageable);
        return getChatRoomDtos(userCreateAll);
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
    public void joinRoom(Long roomId, Long userId) {
        RLock lock = redissonClient.getLock("joinRoomLock:" + roomId);
        try {
            boolean available = lock.tryLock(1, TimeUnit.SECONDS);

            if (!available) {
                throw new CustomException(ErrorCode.FAILED_GET_LOCK);
            }
            // 유저 조회
            User findUser = getUser(userId);

            // room 조회
            ChatRoom chatRoom = chatRoomRepository.findById(roomId) // lock (기존)
                    .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ROOM));

            // user_chatroom 현재 인원 카운트 (비즈니스 로직)
            Long currentUserCount = userChatRoomRepository.countNonLockByChatRoomId(roomId); // lock (기존)

            List<Long> userChatRoomByChatRoomId = userChatRoomRepository
                    .findUserChatRoomByChatRoom_Id(roomId);

            if (userChatRoomByChatRoomId.contains(userId)) {
                throw new CustomException(ErrorCode.ALREADY_JOIN_ROOM);
            }

            // chatroom 입장
            if (currentUserCount >= chatRoom.getUserCountMax()) {
                throw new CustomException(ErrorCode.ROOM_USER_FULL);
            }

            UserChatRoom userChatRoom = UserChatRoom.builder()
                    .user(findUser)
                    .chatRoom(chatRoom)
                    .build();
            userChatRoomRepository.save(userChatRoom);
            // 비즈니스 로직 끝
        } catch (InterruptedException e) {
            throw new CustomException(ErrorCode.FAILED_GET_LOCK);
        } finally {
            lock.unlock();
        }


    }

    @Override
    @Transactional
    public void outRoom(Long userId, Long roomId) {
        ChatRoom room = getChatRoom(roomId);
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
        ChatRoom room = getChatRoom(roomId);
        //방장이 아닐 경우, 삭제 불가
        if (!Objects.equals(room.getRoomCreator(), userId)) {
            throw new CustomException(ErrorCode.NOT_ROOM_CREATOR);
        }
        //모든 사용자 제거 후 방 삭제
        chatMessageRepository.deleteChatMsgByChatRoom_Id(roomId);
        userChatRoomRepository.deleteUserChatRoomByChatRoom_Id(roomId);
        chatRoomRepository.deleteById(roomId);

    }

    @Override
    public void updateRoom(Long roomId, ChatRoomUpdateRequest chatRoomUpdateRequest, Long userId) {
        ChatRoom room = getChatRoom(roomId);
        String currentRoomTitle = room.getTitle();
        if (!room.getRoomCreator().equals(userId)) {
            throw new CustomException(ErrorCode.NOT_ROOM_CREATOR);
        }
        if (currentRoomTitle.equals(chatRoomUpdateRequest.getTitle())) {
            throw new CustomException(ErrorCode.REQUEST_SAME_AS_CURRENT_TITLE);
        }
        room.update(chatRoomUpdateRequest.getTitle(), LocalDateTime.now());
        chatRoomRepository.save(room);
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
    }


    private ChatRoom getChatRoom(Long roomId) {
        return chatRoomRepository
                .findChatRoomById(roomId)
                .orElseThrow(() -> new CustomException(ErrorCode.NONE_ROOM));

    }

    @Override
    @Transactional(readOnly = true)
    public List<ChatRoomUserDto> getRoomUsers(Long roomId, Long userId) {
        // 방 정보
        getChatRoom(roomId);
        // 로그인 유저 정보
        getUser(userId);
        // 방에 있는 유저 정보
        List<UserChatRoom> userIds = userChatRoomRepository
                .findUserChatRoomByChatRoomId(roomId);
        // 방에 있지 않은 유저는 볼 수 없음
        List<Long> userIdList = new ArrayList<>();
        for (UserChatRoom chatRoom : userIds) {
            Long id = chatRoom.getUser().getId();
            userIdList.add(id);
        }
        if (!userIdList.contains(userId)) {
            throw new CustomException(ErrorCode.NOT_ROOM_MEMBER);
        }
        // DTO 담기
        List<ChatRoomUserDto> chatRoomUserDtos = new ArrayList<>();
        for (UserChatRoom userChatRoom : userIds) {
            ChatRoomUserDto build = ChatRoomUserDto.builder()
                    .nickname(userChatRoom.getUser().getNickname())
                    .status(userChatRoom.getUser().getStatus())
                    .build();
            chatRoomUserDtos.add(build);
        }
        return chatRoomUserDtos;
    }

    // 좋아요 순으로 정렬 후 방 전체 조회
    @Override
    @Transactional(readOnly = true)
    public List<ChatRoomDto> getRoomHeartSortList(Pageable pageable) {
        Page<ChatRoom> all = chatRoomRepository.findAllByOrderByHearts(pageable);
        List<ChatRoom> chatRooms = all.toList();
        List<ChatRoomDto> chatRoomDtos = new ArrayList<>();
        for (ChatRoom chatRoom : chatRooms) {
            ChatRoomDto build = ChatRoomDto.builder()
                    .id(chatRoom.getId())
                    .title(chatRoom.getTitle())
                    .userCountMax(chatRoom.getUserCountMax())
                    .currentUserCount((long) chatRoom.getUserChatRooms().size())
                    .heartCount(chatRoom.getHearts().size())
                    .build();
            chatRoomDtos.add(build);
        }
        return chatRoomDtos;
    }

}
