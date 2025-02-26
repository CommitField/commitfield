package cmf.commitField.domain.pet.service;

import cmf.commitField.domain.pet.entity.Pet;
import cmf.commitField.domain.pet.entity.UserPet;
import cmf.commitField.domain.pet.repository.UserPetRepository;
import cmf.commitField.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserPetService {

    private final UserPetRepository userPetRepository;

    // 유저가 펫을 부화
    public UserPet hatchPet(User user, Pet pet) {
        // 기존에 같은 펫을 부화한 적이 있는지 확인
        boolean alreadyHatched = userPetRepository.existsByUserAndPet(user, pet);

        if (!alreadyHatched) {
            UserPet userPet = new UserPet(user, pet, true);
            return userPetRepository.save(userPet);
        }
        return null;  // 이미 부화한 펫이면 저장하지 않음
    }

    // 유저의 도감 조회 (보유한 펫 목록)
    public List<Pet> getUserPetCollection(User user) {
        List<UserPet> userPets = userPetRepository.findByUser(user);
        return userPets.stream()
                .map(UserPet::getPet)  // UserPet에서 Pet만 추출
                .collect(Collectors.toList());
    }
}
