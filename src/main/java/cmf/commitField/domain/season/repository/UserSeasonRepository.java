package cmf.commitField.domain.season.repository;

import cmf.commitField.domain.season.entity.UserSeason;
import cmf.commitField.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserSeasonRepository extends JpaRepository<UserSeason, Long> {
    List<UserSeason> findByUser(User user);
}
