package cmf.commitField.domain.pet.repository;

import cmf.commitField.domain.pet.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {
    Optional<Pet> findById(Long id);
    List<Pet> findByUserEmail(String email);
    List<Pet> findByUserUsername(String username);
}
