package cmf.commitField.domain.chat.chatMessage.repository;

import cmf.commitField.domain.chat.chatMessage.entity.ChatMsg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMsg, Long> {
    void deleteChatMsgByChatRoom_Id(Long chatRoomId);
}
