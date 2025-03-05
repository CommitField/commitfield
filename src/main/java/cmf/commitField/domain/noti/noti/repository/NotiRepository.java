package cmf.commitField.domain.noti.noti.repository;

import cmf.commitField.domain.noti.noti.entity.Noti;
import cmf.commitField.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotiRepository extends JpaRepository<Noti, Long> {
    Optional<List<Noti>> findNotiByReceiverAndRelId(User receiver, long season);
    Optional<List<Noti>> findNotiByReceiverAndIsRead(User receiver, boolean read);
}
