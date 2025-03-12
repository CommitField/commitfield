package cmf.commitField.domain.pet.repository;

import cmf.commitField.domain.pet.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {
    Optional<Pet> findById(Long id);
    List<Pet> findByUserEmail(String email);
    List<Pet> findByUserUsername(String username);

    @Query("SELECT p FROM Pet p WHERE p.user.email = :email ORDER BY p.id DESC")
    List<Pet> findLatestPetByUserEmail(@Param("email") String email);
    @Query("SELECT p FROM Pet p WHERE p.user.username = :username ORDER BY p.id DESC")
    List<Pet> findLatestPetByUserUsername(@Param("username") String username);
}
