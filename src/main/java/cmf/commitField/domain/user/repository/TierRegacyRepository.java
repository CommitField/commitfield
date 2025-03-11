package cmf.commitField.domain.user.repository;

import cmf.commitField.domain.user.entity.TierRegacy;
import cmf.commitField.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TierRegacyRepository extends JpaRepository<TierRegacy, Long>{
    Optional<TierRegacy> findById(Long id);
    List<TierRegacy> findByUserUsername(String username);
    List<TierRegacy> findByUser(User user);
}
