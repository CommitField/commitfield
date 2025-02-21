package cmf.commitField.domain.season.repository;

import cmf.commitField.domain.mock.user.entity.MockUser;
import cmf.commitField.domain.season.entity.UserSeason;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserSeasonRepository extends JpaRepository<UserSeason, Long> {
    List<UserSeason> findByUser(MockUser user);
}
