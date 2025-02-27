package cmf.commitField.domain.commit.scheduler;

import cmf.commitField.domain.commit.sinceCommit.service.CommitCacheService;
import cmf.commitField.domain.commit.totalCommit.service.TotalCommitService;
import cmf.commitField.domain.redpanda.RedpandaProducer;
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
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommitScheduler {
    private final TotalCommitService totalCommitService;
    private final CommitCacheService commitCacheService;
    private final RedpandaProducer redpandaProducer;
    private final UserRepository userRepository;
    private final StringRedisTemplate redisTemplate;
    private final AtomicInteger counter = new AtomicInteger(0); // 카운터 변수
    @Scheduled(fixedRate = 60000) // 1분마다 실행
    public void updateUserCommits() {
        // 최근 3시간 이내에 커밋 기록이 없는 유저는 주기적으로 검사하지 않고, 10분에 한 번씩 검사함 -> 전체 검사
        // 최근 3시간 이내에 커밋 기록이 있는 유저는 1분에 한번씩 검사함. -> 일부 검사
        // Redis에 커밋 기록이 있는 유저를 기록하고, 이 유저들에 한해서만 API를 검색함
        // Redis에 저장되지 않은 유저는 1시간에 한 번씩 검사하는 메소드를 실행
        // 검사를 실행한 후, 커밋 기록이 갱신된 유저는 반영 후 Redis에 3시간동안 지속되게끔 값을 생성해준다.
        log.info("🔍 updateUserCommits 실행중");
        int count = counter.incrementAndGet(); // 실행 횟수 증가

        if (count % 10 == 0) {
            // 🔹 10분마다 전체 유저 검색
            List<User> allUsers = userRepository.findAll();
            log.info("🔍 All User Count: {}", allUsers.size());

            for (User user : allUsers) {
                // 🔹 API에서 해당 유저의 최근 커밋 개수 가져오기
                long newCommitCount = totalCommitService.getSeasonCommits(
                        user.getUsername(),
                        user.getLastCommitted(),
                        LocalDateTime.now()
                ).getTotalCommitContributions();

                if (newCommitCount > 0) {
                    // 🔹 커밋 기록이 있으면 DB, Redis, 메시지 큐 반영
                    updateCommitData(user, newCommitCount);
                }

                log.info("🔍 User: {}, Commit Count: {}", user.getUsername(), newCommitCount);
            }
        } else {
            // 🔹 1분마다 Redis에 저장된 유저만 검색
            Set<String> activeUsers = redisTemplate.keys("commit_active:*"); // Redis에서 저장된 유저 키 가져오기
            log.info("🔍 Active User Count: {}", activeUsers.size());

            for (String key : activeUsers) {
                String username = key.replace("commit_active:", ""); // Redis 키에서 유저명 추출
                User user = userRepository.findByUsername(username).orElse(null);
                if (user == null) continue;

                // 🔹 API에서 해당 유저의 최근 커밋 개수 가져오기
                long newCommitCount = totalCommitService.getSeasonCommits(
                        user.getUsername(),
                        user.getLastCommitted(),
                        LocalDateTime.now()
                ).getTotalCommitContributions();

                if (newCommitCount > 0) {
                    // 🔹 커밋 기록이 있으면 DB, Redis, 메시지 큐 반영
                    updateCommitData(user, newCommitCount);
                }

                log.info("🔍 Active User: {}, Commit Count: {}", user.getUsername(), newCommitCount);
            }
        }
    }

    // 🔹 커밋 기록이 있으면 DB + Redis + 메시지 큐 반영하는 메소드
    private void updateCommitData(User user, long newCommitCount) {
        // 1️⃣ DB의 lastCommitted 갱신
        user.setLastCommitted(LocalDateTime.now());
        userRepository.save(user);

        // 2️⃣ Redis에 갱신 (3시간 동안 유지)
        commitCacheService.updateCachedCommitCount(user.getUsername(), newCommitCount);

        // 3️⃣ 레드판다 메시지 전송
        redpandaProducer.sendCommitUpdate(user.getUsername(), newCommitCount);

        log.info("✅ 커밋 반영 완료 - User: {}, New Commits: {}", user.getUsername(), newCommitCount);
    }

}
