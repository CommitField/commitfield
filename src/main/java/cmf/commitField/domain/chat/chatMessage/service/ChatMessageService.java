package cmf.commitField.domain.chat.chatMessage.service;

import cmf.commitField.domain.chat.chatMessage.controller.request.ChatMsgRequest;
import cmf.commitField.domain.chat.chatMessage.controller.response.ChatMsgResponse;
import cmf.commitField.domain.chat.chatMessage.dto.ChatMsgDto;

import java.util.List;

public interface ChatMessageService {
    ChatMsgResponse sendMessage(ChatMsgRequest message, Long userId, Long roomId);

    List<ChatMsgDto> getRoomChatMsgList(Long roomId, Long userId, Long lastId);
}
