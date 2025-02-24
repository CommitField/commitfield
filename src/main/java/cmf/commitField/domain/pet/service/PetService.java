package cmf.commitField.domain.pet.service;

import cmf.commitField.domain.pet.entity.Pet;
import cmf.commitField.domain.pet.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PetService {

    private final PetRepository petRepository;

    // 새로운 펫 생성
    public Pet createPet(String name, String imageUrl) {
        Pet pet = new Pet(name, imageUrl);
        return petRepository.save(pet);
    }

    // 모든 펫 조회
    public List<Pet> getAllPets() {
        return petRepository.findAll();
    }

    // 특정 펫 조회
    public Pet getPetById(Long petId) {
        Optional<Pet> pet = petRepository.findById(petId);
        return pet.orElse(null);
    }

    // 펫 삭제
    public void deletePet(Long petId) {
        petRepository.deleteById(petId);
    }

}
