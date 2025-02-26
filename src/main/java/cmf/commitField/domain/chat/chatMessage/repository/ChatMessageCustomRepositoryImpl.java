package cmf.commitField.domain.chat.chatMessage.repository;

import cmf.commitField.domain.chat.chatMessage.entity.ChatMsg;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ChatMessageCustomRepositoryImpl implements ChatMessageCustomRepository {

    private final EntityManager entityManager;

    @Override
    public List<ChatMsg> findChatRoomIdByChatMsg(Long chatMsg, Long lastId) {
        String first = "select c from ChatMsg c where c.chatRoom.id =: chatMsg order by c.id asc";
        String paging = "select c from ChatMsg c where c.chatRoom.id =: chatMsg and c.id > :lastId order by c.id asc";

        TypedQuery<ChatMsg> query = null;

        if (lastId == null) {
            query = entityManager
                    .createQuery(first, ChatMsg.class)
                    .setParameter("chatMsg", chatMsg);
        } else {
            query = entityManager
                    .createQuery(paging, ChatMsg.class)
                    .setParameter("chatMsg", chatMsg)
                    .setParameter("lastId", lastId);
        }
        return query
                .setMaxResults(10)
                .getResultList();
    }

}
