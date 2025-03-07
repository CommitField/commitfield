package cmf.commitField.domain.heart.service;

import cmf.commitField.domain.chat.chatRoom.entity.ChatRoom;
import cmf.commitField.domain.chat.chatRoom.repository.ChatRoomRepository;
import cmf.commitField.domain.heart.entity.Heart;
import cmf.commitField.domain.heart.repository.HeartRepository;
import cmf.commitField.domain.user.entity.User;
import cmf.commitField.domain.user.repository.UserRepository;
import cmf.commitField.global.error.ErrorCode;
import cmf.commitField.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HeartServiceImpl implements HeartService {
    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final HeartRepository heartRepository;

    @Override
    public void heart(Long userId, Long roomId) {
        Optional<User> user = userRepository
                .findById(userId);
        Optional<ChatRoom> room = chatRoomRepository
                .findById(roomId);
        if (user.isEmpty()) {
            throw new CustomException(ErrorCode.MEMBER_NOT_FOUND);
        }
        if (room.isEmpty()) {
            throw new CustomException(ErrorCode.NOT_FOUND_ROOM);
        }
        Optional<Heart> userToRoomHeart = heartRepository
                .findByUserIdAndChatRoomId(userId, roomId);
        if (userToRoomHeart.isPresent()) {
            throw new CustomException(ErrorCode.ALREADY_HEART_TO_ROOM);
        }
        Heart build = Heart.builder()
                .user(user.get())
                .chatRoom(room.get())
                .build();
        heartRepository.save(build);
    }

    @Override
    @Transactional
    public void heartDelete(Long userId, Long roomId) {
        Optional<User> user = userRepository
                .findById(userId);
        Optional<ChatRoom> room = chatRoomRepository
                .findById(roomId);
        if (user.isEmpty()) {
            throw new CustomException(ErrorCode.MEMBER_NOT_FOUND);
        }
        if (room.isEmpty()) {
            throw new CustomException(ErrorCode.NOT_FOUND_ROOM);
        }
        Optional<Heart> userToRoomHeart = heartRepository
                .findByUserIdAndChatRoomId(userId, roomId);
        if (userToRoomHeart.isEmpty()) {
            throw new CustomException(ErrorCode.NOT_EXIST_ROOM_HEART);
        }
        Heart heart = userToRoomHeart.get();
        heartRepository.delete(heart);
    }
}