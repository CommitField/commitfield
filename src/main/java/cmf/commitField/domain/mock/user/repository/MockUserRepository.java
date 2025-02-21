package cmf.commitField.domain.mock.user.repository;

import cmf.commitField.domain.mock.user.entity.MockUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MockUserRepository extends JpaRepository<MockUser, Long> {
    MockUser findByNickname(String nickname);
}
