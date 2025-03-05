package cmf.commitField.domain.heart.repository;

import cmf.commitField.domain.heart.entity.Heart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HeartRepository extends JpaRepository<Heart, Long> {
    Optional<Heart> findByUserIdAndChatRoomId(Long userId, Long chatRoomId);

    List<Heart> findByUserId(Long userId);
}
