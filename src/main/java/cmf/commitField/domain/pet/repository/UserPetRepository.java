package cmf.commitField.domain.pet.repository;

import cmf.commitField.domain.pet.entity.Pet;
import cmf.commitField.domain.pet.entity.UserPet;
import cmf.commitField.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserPetRepository extends JpaRepository<UserPet, Long> {
    List<UserPet> findByUser(User user);
    boolean existsByUserAndPet(User user, Pet pet);
}
