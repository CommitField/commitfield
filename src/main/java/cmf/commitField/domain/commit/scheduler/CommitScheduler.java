package cmf.commitField.domain.commit.scheduler;

import cmf.commitField.domain.commit.sinceCommit.service.CommitCacheService;
import cmf.commitField.domain.commit.sinceCommit.service.GithubService;
import cmf.commitField.domain.redpanda.RedpandaProducer;
import cmf.commitField.domain.user.entity.User;
import cmf.commitField.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommitScheduler {
    private final GithubService githubService;
    private final CommitCacheService commitCacheService;
    private final RedpandaProducer redpandaProducer;
    private final UserRepository userRepository;

    @Scheduled(fixedRate = 60000) // 1분마다 실행
    public void updateUserCommits() {
        List<User> activeUsers = userRepository.findActiveUser(); // 💫 변경 필요, 차후 active 상태인 user만 찾게끔 변경해야 함.

        for (User user : activeUsers) {
            Integer cachedCount = commitCacheService.getCachedCommitCount(user.getUsername());
            int newCommitCount = githubService.getUserCommitCount(user.getUsername());

            if (cachedCount == null || cachedCount != newCommitCount) { // 변화가 있을 때만 처리
                commitCacheService.updateCachedCommitCount(user.getUsername(), newCommitCount);
                redpandaProducer.sendCommitUpdate(user.getUsername(), newCommitCount);
            }
        }
    }
}
