package cmf.commitField.domain.chat.chatMessage.repository;

import cmf.commitField.domain.chat.chatMessage.entity.ChatMsg;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageCustomRepository {
    List<ChatMsg> findChatRoomIdByChatMsg(Long chatMsg, Long lastId);
}
