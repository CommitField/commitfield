package cmf.commitField.domain.noti.noti.repository;

import cmf.commitField.domain.noti.noti.dto.NotiDto;
import cmf.commitField.domain.noti.noti.entity.Noti;
import cmf.commitField.domain.user.entity.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotiRepository extends JpaRepository<Noti, Long> {
    Optional<List<Noti>> findNotiByReceiverAndRelId(User receiver, long season);
    @Query("SELECT new cmf.commitField.domain.noti.noti.dto.NotiDto(n.message, n.createdAt) " +
            "FROM Noti n JOIN n.receiver u WHERE u.id = :receiverId AND n.isRead = :isRead")
    Optional<List<NotiDto>> findNotiDtoByReceiverId(@Param("receiverId") Long receiverId, @Param("isRead") boolean isRead);

}
