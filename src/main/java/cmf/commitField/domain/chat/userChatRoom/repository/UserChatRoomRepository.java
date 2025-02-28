package cmf.commitField.domain.chat.userChatRoom.repository;

import cmf.commitField.domain.chat.userChatRoom.entity.UserChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserChatRoomRepository extends JpaRepository<UserChatRoom, Long> {

//    @Lock(LockModeType.PESSIMISTIC_WRITE)
//    @QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value = "1000")})
    Long countByChatRoomId(Long roomId); // 비관적 락

//    @Query("select count(*) from UserChatRoom u where u.chatRoom.id = ?1 ")
    @Query("select count(*) from UserChatRoom u where u.chatRoom.id = :roomId")
    Long countNonLockByChatRoomId(@Param("roomId")Long roomId); // test 용도
    // 특정 방의 모든 유저 관계 삭제
    void deleteUserChatRoomByChatRoom_Id(Long chatRoomId);
    // 특정 방에서 특정 사용자만 삭제
    void deleteUserChatRoomByChatRoom_IdAndUserId(Long chatRoomId, Long userId);
    // 특정 방과 사용자 관계 삭제
    void deleteUserChatRoomByUserId(Long userId);
    // 사용자가 해당 방에 참여한 여부 확인
    boolean existsByChatRoomIdAndUserId(Long roomId, Long userId);
    // 특정 방에 참여한 모든 UserChatRoom 관계 조회
    List<UserChatRoom> findByChatRoom_Id(Long chatRoomId);
    @Query("select u.user.id from UserChatRoom u where u.chatRoom.id = ?1")
    List<Long> findUserChatRoomByChatRoom_Id(Long chatRoomId);

    List<UserChatRoom> findUserChatRoomByChatRoomId(Long roomId);
    //out room 조회
    List<UserChatRoom> findUserByChatRoomId(Long roomId);

}
