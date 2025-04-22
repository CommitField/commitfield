package cmf.commitField.domain.noti.noti.repository;

import cmf.commitField.domain.noti.noti.dto.NotiDto;
import cmf.commitField.domain.noti.noti.entity.Noti;
import cmf.commitField.domain.noti.noti.entity.NotiDetailType;
import cmf.commitField.domain.noti.noti.entity.NotiType;
import cmf.commitField.domain.user.entity.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface NotiRepository extends JpaRepository<Noti, Long> {
    Optional<List<Noti>> findNotiByReceiverAndRelId(User receiver, long season);
    @Query("SELECT new cmf.commitField.domain.noti.noti.dto.NotiDto(n.id, n.message, n.createdAt) " +
            "FROM Noti n JOIN n.receiver u WHERE u.id = :receiverId AND n.isRead = :isRead")
    Optional<List<NotiDto>> findNotiDtoByReceiverId(@Param("receiverId") Long receiverId, @Param("isRead") boolean isRead);
    Optional<List<Noti>> findNotiByReceiver(User receiver);

    // 최근 10일 내 동일한 커밋 부재 알림이 있는지 확인
    boolean existsByReceiverAndTypeCodeAndType2CodeAndCreatedAtAfter(User receiver, NotiType type, NotiDetailType detailType, LocalDateTime after);

    // 해당 타입의 알림이 오늘 이후에 있는지 확인
    boolean existsByReceiverAndType2CodeAndCreatedAtAfter(User receiver, NotiDetailType notiDetailType, LocalDateTime todayStart);
}