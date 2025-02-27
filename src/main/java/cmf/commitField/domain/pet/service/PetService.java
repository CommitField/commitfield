package cmf.commitField.domain.pet.service;

import cmf.commitField.domain.pet.entity.Pet;
import cmf.commitField.domain.pet.repository.PetRepository;
import cmf.commitField.domain.user.entity.User;
import cmf.commitField.global.aws.s3.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class PetService {

    private final PetRepository petRepository;
    private final S3Service s3Service;

    // 새로운 펫 생성
    public Pet createPet(String name, MultipartFile imageFile, User user) throws IOException {

        // ✅ S3 업로드 로직 추가
        String imageUrl = null;
        if (imageFile != null && !imageFile.isEmpty()) {
            imageUrl = s3Service.uploadFile(imageFile, "pet-images");
        }
        Random random = new Random();
        Pet pet = new Pet(name, user);
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
