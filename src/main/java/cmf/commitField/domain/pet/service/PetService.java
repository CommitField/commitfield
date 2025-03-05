package cmf.commitField.domain.pet.service;

import cmf.commitField.domain.pet.dto.UserPetDto;
import cmf.commitField.domain.pet.entity.Pet;
import cmf.commitField.domain.pet.entity.PetGrow;
import cmf.commitField.domain.pet.repository.PetRepository;
import cmf.commitField.domain.user.entity.User;
import cmf.commitField.domain.user.repository.UserRepository;
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
    private final UserRepository userRepository;
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

    // 펫 성장
    public UserPetDto getExpPet(String username, int commitCount) {
        User user = userRepository.findByUsername(username).get();
        Pet pet = user.getPets().get(0);
        pet.addExp(commitCount); // 경험치 증가

        // 경험치 증가 후, 만약 레벨업한다면 레벨업 시킨다.
        if( (pet.getGrow()== PetGrow.EGG && pet.getExp()>= PetGrow.EGG.getRequiredExp()) ||
                (pet.getGrow()== PetGrow.HATCH && pet.getExp()>= PetGrow.HATCH.getRequiredExp())) {
            System.out.println("펫 레벨 업, 현재 경험치 : "+pet.getExp());
            levelUp(pet);
        }

        return UserPetDto.builder().
                username(username).
                petId(pet.getId()).
                petName(pet.getName()).
                exp(pet.getExp()).
                grow(String.valueOf(pet.getGrow())).
                build();
    }

    // 펫 레벨 업
    public void levelUp(Pet pet){
        switch (pet.getGrow()){
            case EGG :
                pet.setGrow(PetGrow.HATCH);
                break;
            case HATCH :
                pet.setGrow(PetGrow.GROWN);
                break;
        }
    }
}
