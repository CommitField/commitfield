package cmf.commitField.domain.commit.scheduler;

import cmf.commitField.domain.commit.totalCommit.service.TotalCommitService;
import cmf.commitField.domain.noti.noti.service.NotiService;
import cmf.commitField.domain.user.dto.UserInfoDto;
import cmf.commitField.domain.user.entity.Tier;
import cmf.commitField.domain.user.entity.User;
import cmf.commitField.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CommitUpdateService {
    private final TotalCommitService totalCommitService;
    private final UserRepository userRepository;
    private final NotiService notiService;

    // 유저 랭킹 상승 로직
    public UserInfoDto updateUserTier(String username){
        User user = userRepository.findByUsername(username).get();
        long seasonCommitCount;
        seasonCommitCount = totalCommitService.getSeasonCommits(user.getUsername(), LocalDateTime.of(2025,03,01,0,0), LocalDateTime.of(2025,05,31,23,59)).getTotalCommitContributions();
        user.setTier(Tier.getLevelByExp((int)seasonCommitCount));
        System.out.println(username+"유저 레벨 업! 현재 티어: "+user.getTier());
        userRepository.save(user);

        notiService.createRankUpNoti(user);

        return UserInfoDto.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .avatarUrl(user.getAvatarUrl())
                .createdAt(user.getCreatedAt())
                .lastCommitted(user.getLastCommitted())
                .commitCount(user.getCommitCount())
                .tier(user.getTier().name())
                .build();
    }

}
