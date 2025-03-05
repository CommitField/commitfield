package cmf.commitField.domain.user.repository;

import cmf.commitField.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);

    @Modifying
    @Query("UPDATE User u SET u.status = :status WHERE u.username = :username")
    void updateStatus(@Param("username") String username, @Param("status") boolean status);
}
