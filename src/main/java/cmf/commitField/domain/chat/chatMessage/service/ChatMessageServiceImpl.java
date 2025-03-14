package cmf.commitField.domain.chat.chatMessage.service;

import cmf.commitField.domain.chat.chatMessage.controller.request.ChatMsgRequest;
import cmf.commitField.domain.chat.chatMessage.controller.response.ChatMsgResponse;
import cmf.commitField.domain.chat.chatMessage.dto.ChatMsgDto;
import cmf.commitField.domain.chat.chatMessage.entity.ChatMsg;
import cmf.commitField.domain.chat.chatMessage.repository.ChatMessageCustomRepository;
import cmf.commitField.domain.chat.chatMessage.repository.ChatMessageRepository;
import cmf.commitField.domain.chat.chatRoom.entity.ChatRoom;
import cmf.commitField.domain.chat.chatRoom.repository.ChatRoomRepository;
import cmf.commitField.domain.chat.userChatRoom.entity.UserChatRoom;
import cmf.commitField.domain.chat.userChatRoom.repository.UserChatRoomRepository;
import cmf.commitField.domain.user.entity.User;
import cmf.commitField.domain.user.repository.UserRepository;
import cmf.commitField.global.error.ErrorCode;
import cmf.commitField.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageCustomRepository chatMessageCustomRepository;
    private final UserChatRoomRepository userChatRoomRepository;

    // 커밋용 주석

    @Override
    @Transactional
    public ChatMsgResponse sendMessage(ChatMsgRequest message, Long userId, Long roomId) {
        User findUser = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        ChatRoom chatRoom = chatRoomRepository.findChatRoomById(roomId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ROOM));

        ChatMsg chatMsg = ChatMsg.builder()
                .message(message.getMessage())
                .createdAt(LocalDateTime.now())
                .user(findUser)
                .chatRoom(chatRoom)
                .build();

        chatMessageRepository.save(chatMsg);

        // Response message
        // 응답값 변환
        return ChatMsgResponse.builder()
                .id(chatMsg.getId())         // chatMsgId 추가
                .roomId(roomId)
                .userId(userId)              // userId 추가
                .from(findUser.getUsername())
                .message(message.getMessage())
                .sendAt(chatMsg.getCreatedAt())
                .avatarUrl(findUser.getAvatarUrl()) // 아바타 URL 추가
                .build();

    }

    @Transactional(readOnly = true)
    @Override
    public List<ChatMsgDto> getRoomChatMsgList(Long roomId, Long userId, Long lastId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        List<ChatMsg> chatMsgsList = chatMessageCustomRepository.findChatRoomIdByChatMsg(roomId, lastId);
        Optional<UserChatRoom> joinUser = userChatRoomRepository.findByUserIdAndChatRoomId(userId,roomId);
        if (joinUser.isEmpty()) {
            throw new CustomException(ErrorCode.NOT_FOUND_USER);
        }
        LocalDateTime joinDt = joinUser.get().getJoinDt();
        List<ChatMsgDto> chatMsgDtos = new ArrayList<>();
        for (ChatMsg chatMsg : chatMsgsList) {
            ChatMsgDto build = ChatMsgDto.builder()
                    .chatMsgId(chatMsg.getId())
                    .nickname(chatMsg.getUser().getUsername()) // nickname 대신 username 사용
                    .sendAt(chatMsg.getCreatedAt())
                    .message(chatMsg.getMessage())
                    .userId(chatMsg.getUser().getId())
                    .avatarUrl(chatMsg.getUser().getAvatarUrl()) // avatarUrl 추가
                    .build();
            if (build.getSendAt().isAfter(joinDt)) {
                chatMsgDtos.add(build);
            }
        }
        return chatMsgDtos;
    }
}