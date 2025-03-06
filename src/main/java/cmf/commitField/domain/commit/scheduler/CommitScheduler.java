package cmf.commitField.domain.commit.scheduler;

import cmf.commitField.domain.commit.totalCommit.service.TotalCommitService;
import cmf.commitField.domain.user.entity.User;
import cmf.commitField.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommitScheduler {
    private final TotalCommitService totalCommitService;
    private final UserRepository userRepository;
    private final StringRedisTemplate redisTemplate;
    private final AtomicInteger counter = new AtomicInteger(0);

    private final ApplicationEventPublisher eventPublisher;


    @Scheduled(fixedRate = 60000) // 1분마다 실행
    public void updateUserCommits() {
        log.info("🔍 updateUserCommits 실행중");
        int count = counter.incrementAndGet();

        // 최근 로그인이 이루어진 (접근 가능성이 높은) 유저만 실시간으로 커밋 수 변경 추적 후 갱신
        // 최근 로그인이 이루어지지 않은 유저는 페이지 최초 로그인 시 갱신이 발생한다.
        Set<String> activeUsers = redisTemplate.keys("commit_active:*");
        log.info("🔍 Active User Count: {}", activeUsers.size());

        // 현재 접속 기록이 있는 유저, 커밋 기록이 있는 유저는 주기적으로 갱신
        for (String key : activeUsers) {
            String username = key.replace("commit_active:", "");

            String lastcmKey = "commit_lastCommitted:" + username; // active유저의 key
            String lastCommitted = redisTemplate.opsForValue().get(lastcmKey); // 마지막 커밋 시간

            System.out.println("username: "+username+"/ user lastCommitted: "+lastCommitted);
            if(username!=null && lastCommitted!=null) processUserCommit(username);
        }
    }

    // 🔹 유저 커밋 검사 및 반영
    private void processUserCommit(String username) {
        // 유저가 접속한 동안 추가한 commit수를 확인.
        String activeKey = "commit_active:" + username; // active유저의 key
        String lastcmKey = "commit_lastCommitted:" + username; // active유저의 key
        Long currentCommit = Long.parseLong(redisTemplate.opsForValue().get(activeKey)); // 현재까지 확인한 커밋 개수
        String lastcommitted = redisTemplate.opsForValue().get(lastcmKey); // 마지막 커밋 시간
        long updateTotalCommit, newCommitCount;

        // 현재 커밋 개수 조회
        updateTotalCommit = totalCommitService.getTotalCommitCount(
            username
        ).getTotalCommitContributions();

        newCommitCount = updateTotalCommit - currentCommit; // 새로 추가된 커밋 수

        if(newCommitCount > 0){
            User user = userRepository.findByUsername(username).get();
            LocalDateTime now = LocalDateTime.now();
            //커밋 수가 갱신된 경우, 이 시간을 기점으로 lastCommitted를 변경한다.
            user.setLastCommitted(now);
            userRepository.save(user);

            redisTemplate.opsForValue().set(activeKey, String.valueOf(updateTotalCommit), 3, TimeUnit.HOURS);
            redisTemplate.opsForValue().set(lastcmKey, String.valueOf(now), 3, TimeUnit.HOURS);

            CommitUpdateEvent event = new CommitUpdateEvent(this, username, newCommitCount);
            eventPublisher.publishEvent(event); // 이벤트 발생
            System.out.println("CommitCreatedEvent published for user: " + username);
        }

        // FIXME: 차후 리팩토링 필요
        log.info("🔍 User: {}, LastCommitted: {}, New Commits: {}, Total Commits: {}", username, lastcommitted, newCommitCount, updateTotalCommit);
    }
}
