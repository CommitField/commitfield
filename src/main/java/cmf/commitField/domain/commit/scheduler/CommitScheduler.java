package cmf.commitField.domain.commit.scheduler;

import cmf.commitField.domain.commit.totalCommit.service.TotalCommitService;
import cmf.commitField.domain.user.entity.User;
import cmf.commitField.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommitScheduler {
    private final TotalCommitService totalCommitService;
    private final CommitCacheService commitCacheService;
    private final UserRepository userRepository;
    private final StringRedisTemplate redisTemplate;
    private final AtomicInteger counter = new AtomicInteger(0);

    @Scheduled(fixedRate = 60000) // 1분마다 실행
    public void updateUserCommits() {
        log.info("🔍 updateUserCommits 실행중");
        int count = counter.incrementAndGet();

        if (count % 10 == 0) {
            List<User> allUsers = userRepository.findAll();
            log.info("🔍 All User Count: {}", allUsers.size());

            for (User user : allUsers) {
                processUserCommit(user);
            }
        } else {
            Set<String> activeUsers = redisTemplate.keys("commit_active:*");
            log.info("🔍 Active User Count: {}", activeUsers.size());

            for (String key : activeUsers) {
                String username = key.replace("commit_active:", "");
                User user = userRepository.findByUsername(username).orElse(null);
                if (user != null) {
                    processUserCommit(user);
                }
            }
        }
    }

    // 🔹 유저 커밋 검사 및 반영
    private void processUserCommit(User user) {
        // Redis에서 lastCommitted 값 가져오기
        String redisKey = "commit_last:" + user.getUsername();
        String lastCommittedStr = redisTemplate.opsForValue().get(redisKey);
        LocalDateTime lastCommitted;
        if(lastCommittedStr != null){
            lastCommitted=LocalDateTime.parse(lastCommittedStr);
        }else{
            user.setLastCommitted(LocalDateTime.now()); // 레디스에 저장되어있지 않았다면 등록 시점에 lastCommitted를 갱신
            lastCommitted=user.getLastCommitted();  // Redis에 없으면 DB값 사용;
        }

        // 현재 커밋 개수 조회
        long currentCommitCount = totalCommitService.getUpdateCommits(
                user.getUsername(),
                lastCommitted,  // 🚀 Redis에 저장된 lastCommitted 기준으로 조회
                LocalDateTime.now()
        ).getTotalCommitContributions();

        // Redis에서 이전 커밋 개수 가져오기
        Integer previousCommitCount = commitCacheService.getCachedCommitCount(user.getUsername());
        long newCommitCount = previousCommitCount == null ? 0 : (currentCommitCount - previousCommitCount);

        if (newCommitCount > 0) {
            updateCommitData(user, currentCommitCount, newCommitCount);
        }

        log.info("🔍 User: {}, New Commits: {}, Total Commits: {}", user.getUsername(), newCommitCount, currentCommitCount);
    }

    // 🔹 새 커밋이 있으면 데이터 업데이트
    private void updateCommitData(User user, long currentCommitCount, long newCommitCount) {
        // 1️⃣ Redis에 lastCommitted 업데이트 (3시간 TTL)
        String redisKey = "commit_last:" + user.getUsername();
        redisTemplate.opsForValue().set(redisKey, LocalDateTime.now().toString(), 3, TimeUnit.HOURS);

        // 2️⃣ Redis에 최신 커밋 개수 저장 (3시간 동안 유지)
        commitCacheService.updateCachedCommitCount(user.getUsername(), currentCommitCount);

        log.info("✅ 커밋 반영 완료 - User: {}, New Commits: {}", user.getUsername(), newCommitCount);
    }
}
