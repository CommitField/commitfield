package cmf.commitField.domain.user.service;

import cmf.commitField.domain.commit.scheduler.CommitUpdateService;
import cmf.commitField.domain.commit.totalCommit.service.TotalCommitService;
import cmf.commitField.domain.pet.entity.Pet;
import cmf.commitField.domain.pet.repository.PetRepository;
import cmf.commitField.domain.pet.service.PetService;
import cmf.commitField.domain.user.dto.UserInfoDto;
import cmf.commitField.domain.user.entity.User;
import cmf.commitField.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class UserService {
    private final StringRedisTemplate redisTemplate;
    private final UserRepository userRepository;
    private final PetRepository petRepository;
    // FIXME: 수정 필요
    private final TotalCommitService totalCommitService;
    private final CommitUpdateService commitUpdateService;
    private final PetService petService;

    @Transactional
    public void updateUserStatus(String username, boolean status) {
        System.out.println("Updating status for " + username + " to " + status);
        userRepository.updateStatus(username, status);
    }

    @Transactional
    public UserInfoDto showUserInfo(String username) {
        User user = userRepository.findByUsername(username).get();
        Pet pet = petRepository.findByUserEmail(user.getEmail()).get(0); // TODO: 확장시 코드 수정 필요

        long totalCommit = totalCommitService.getTotalCommitCount(username).getTotalCommitContributions();
        // 유저 정보 조회 후 변경사항은 업데이트
        // TODO: 스케쥴러 수정 후 펫 부분 수정 필요
        user.setCommitCount(totalCommit);
//        commitUpdateService.updateUserTier(user.getUsername());
//        petService.getExpPet(user.getUsername(), 0);

        String key = "commit_active:" + user.getUsername();
        if(redisTemplate.opsForValue().get(key)==null){
            redisTemplate.opsForValue().set(key, String.valueOf(user.getCommitCount()), 3, TimeUnit.HOURS);
            redisTemplate.opsForValue().set("commit_lastCommitted:" + username, LocalDateTime.now().toString(),3, TimeUnit.HOURS);
        }

        return UserInfoDto.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .avatarUrl(user.getAvatarUrl())
                .tier(user.getTier().toString())
                .commitCount(totalCommit)
                .createdAt(user.getCreatedAt())
                .lastCommitted(user.getLastCommitted())
                .petType(pet.getType())
                .petExp(pet.getExp())
                .petGrow(pet.getGrow().toString())
                .build();
    }

    // 유저 성장
    @Transactional
    public boolean getExpUser(String username, long commitCount) {
        User user = userRepository.findByUsername(username).get();

        // 경험치 증가 후, 만약 레벨업한다면 레벨업 시킨다.
        user.addExp(commitCount);

        return !(user.getTier().equals(User.Tier.getLevelByExp(user.getSeasonCommitCount())));
    }

    public void updateUserCommitCount(String username, long count){
        User user = userRepository.findByUsername(username).get();
        user.addCommitCount(count);
        userRepository.save(user);
    }
}
