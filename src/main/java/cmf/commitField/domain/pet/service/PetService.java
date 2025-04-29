package cmf.commitField.domain.pet.service;

import cmf.commitField.domain.pet.dto.PetsDto;
import cmf.commitField.domain.pet.dto.UserPetDto;
import cmf.commitField.domain.pet.dto.UserPetListDto;
import cmf.commitField.domain.pet.entity.Pet;
import cmf.commitField.domain.pet.entity.PetGrow;
import cmf.commitField.domain.pet.repository.PetRepository;
import cmf.commitField.domain.user.entity.User;
import cmf.commitField.domain.user.repository.UserRepository;
import cmf.commitField.global.aws.s3.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PetService {
    private final UserRepository userRepository;
    private final PetRepository petRepository;
    private final S3Service s3Service;

    // 새로운 펫 생성
    public Pet createPet(String petname, String username) throws IOException {
        Random random = new Random();
        User user = userRepository.findByUsername(username).get();
        Pet pet = petRepository.findLatestPetByUserUsername(username).get(0);
        if(pet.getGrow() != PetGrow.GROWN){
            return null;
        }
        Pet newPet = new Pet("알알", user);
        return petRepository.save(newPet);
    }

    // 유저가 소유한 펫 도감 조회
    public UserPetListDto getUserCollection(String username) {
        List<Pet> pets = petRepository.findByUserUsername(username);
        int[] petsNum = new int[100]; //FIXME: 차후 갯수 수정 필요
        int max = 0;
        Map<Integer, Integer> petList = new HashMap<>();
        for(Pet pet : pets){
            int type = pet.getType();
            petsNum[type]++;
            if(max<type) max = type;
        }

        for(int i=0;i<=max;i++){
            petList.put(i, petsNum[i]);
        }

        return UserPetListDto.builder().
                username(username).
                petList(petList).
                build();
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
    @Transactional
    public UserPetDto getExpPet(String username, long commitCount) {
        User user = userRepository.findByUsername(username).get();
        Pet pet = petRepository.findLatestPetByUserUsername(username).get(0);
        pet.addExp(commitCount); // 경험치 증가
        petRepository.save(pet);

        // 경험치 증가 후, 만약 레벨업한다면 레벨업 시킨다.
        if(!PetGrow.getLevelByExp(pet.getExp()).equals(pet.getGrow())){
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
        pet.setGrow(PetGrow.getLevelByExp(pet.getExp()));
        petRepository.save(pet);
    }

    //유저의 모든 펫 조회
    public List<PetsDto> getAllPets(String username){
        List<Pet> pets = petRepository.findByUserUsername(username);
        List<PetsDto> petsList = new ArrayList<>();
        for(Pet pet : pets){
            petsList.add(
                PetsDto.builder()
                .username(username)
                .petId(pet.getId())
                .petName(pet.getName())
                .type(pet.getType())
                .grow(String.valueOf(pet.getGrow()))
                .build()
            );
        }

        return petsList;
    }
}
