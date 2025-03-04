package cmf.commitField.domain.commit.scheduler;

import cmf.commitField.domain.commit.totalCommit.service.TotalCommitService;
import cmf.commitField.domain.user.entity.User;
import cmf.commitField.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CommitUpdateService {
    TotalCommitService totalCommitService;
    UserRepository userRepository;
    @GetMapping("commit/tier")
    public User updateUserTier(OAuth2User oAuth2User){
        User user = userRepository.findByEmail(oAuth2User.getName()).get();
        long seasonCommitCount;
        seasonCommitCount = totalCommitService.getSeasonCommits(user.getUsername(), LocalDateTime.of(2024,12,01,0,0), LocalDateTime.of(2025,2,28,23,59)).getTotalCommitContributions();
//      seasonCommitCount = totalCommitService.getSeasonCommits(user.getUsername(), LocalDateTime.of(2025,03,01,0,0), LocalDateTime.of(2025,05,31,23,59)).getTotalCommitContributions();
        user.setTier(User.Tier.getLevelByExp((int)seasonCommitCount));
        return user;
    }
}
