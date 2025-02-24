package cmf.commitField.domain.chat.userChatRoom.repository;

import cmf.commitField.domain.chat.userChatRoom.entity.UserChatRoom;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserChatRoomRepository extends JpaRepository<UserChatRoom, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value = "1000")})
    Long countByChatRoomId(Long roomId); // 비관적 락

//    @Query("select count(*) from UserChatRoom u where u.chatRoom.id = ?1 ")
//    Long countNonLockByChatRoomId(Long roomId); // test 용도
    @Query("select count(*) from UserChatRoom u where u.chatRoom.id = :roomId")
    Long countNonLockByChatRoomId(@Param("roomId")Long roomId); // test 용도

    void deleteUserChatRoomByChatRoom_Id(Long chatRoomId);

    void deleteUserChatRoomByUserId(Long userId);
}
