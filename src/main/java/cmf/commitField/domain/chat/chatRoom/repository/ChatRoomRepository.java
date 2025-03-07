package cmf.commitField.domain.chat.chatRoom.repository;

import cmf.commitField.domain.chat.chatRoom.entity.ChatRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

//    @Lock(LockModeType.PESSIMISTIC_WRITE)
//    @QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value ="1000")})
    Optional<ChatRoom> findById(Long aLong);

    Optional<ChatRoom> findChatRoomById(Long id);

    @Query("select c from ChatRoom c where c.roomCreator=:userId")
    Page<ChatRoom> findAllByUserId(@Param("userId")Long userId,Pageable pageable);

    Page<ChatRoom> findAllByUserChatRoomsUserId(Long userId,Pageable pageable);

    @Query(value = "SELECT ROOM_CREATOR FROM chat_room WHERE ID = ?", nativeQuery = true)
    Optional<Long> findChatRoomIdByRoomId(@Param("roomId") Long roomId);

    @Query("SELECT c FROM ChatRoom c LEFT JOIN Heart h ON c.id = h.chatRoom.id " +
            "GROUP BY c.id " +
            "ORDER BY COUNT(h.user.id) DESC")
    Page<ChatRoom> findAllByOrderByHearts(Pageable pageable);

    @Query("SELECT c FROM ChatRoom c WHERE c.id IN :ids ORDER BY c.createdAt DESC")
    Page<ChatRoom> findChatRoomByInId(@Param("ids") List<Long> ids, Pageable pageable);

    @Query("SELECT c FROM ChatRoom c WHERE c.title LIKE CONCAT('%', :title, '%')")
    Page<ChatRoom> findChatRoomWithPartOfTitle(@Param("title") String title, Pageable pageable);

}