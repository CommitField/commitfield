package cmf.commitField.domain.commit.scheduler;

import cmf.commitField.domain.commit.totalCommit.service.TotalCommitService;
import cmf.commitField.domain.pet.service.PetService;
import cmf.commitField.domain.user.dto.UserInfoDto;
import cmf.commitField.domain.user.entity.User;
import cmf.commitField.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CommitUpdateService {
    private final TotalCommitService totalCommitService;
    private final UserRepository userRepository;
    private final PetService petService;

    private final ApplicationEventPublisher eventPublisher;

    public UserInfoDto updateUserTier(String username){
        User user = userRepository.findByUsername(username).get();
        long seasonCommitCount;
//        seasonCommitCount = totalCommitService.getSeasonCommits(user.getUsername(), LocalDateTime.of(2024,12,01,0,0), LocalDateTime.of(2025,2,28,23,59)).getTotalCommitContributions();
      seasonCommitCount = totalCommitService.getSeasonCommits(user.getUsername(), LocalDateTime.of(2025,03,01,0,0), LocalDateTime.of(2025,05,31,23,59)).getTotalCommitContributions();
        user.setTier(User.Tier.getLevelByExp((int)seasonCommitCount));
        userRepository.save(user);

        return UserInfoDto.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .avatarUrl(user.getAvatarUrl())
                .createdAt(user.getCreatedAt())
                .lastCommitted(user.getLastCommitted())
                .commitCount(user.getCommitCount())
                .tier(user.getTier().name())
                .build();
    }

    public User updateUserPet(String username){
        User user = userRepository.findByUsername(username).get();

        //추가된 펫 경험치
        long totalcommits;
        totalcommits = totalCommitService.getUpdateCommits(user.getUsername(), user.getCreatedAt(), LocalDateTime.now()).getTotalCommitContributions();

        petService.getExpPet(user, (int)totalcommits);
        return user;
    }
}
