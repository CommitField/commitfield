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
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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

        // 최근 로그인이 이루어진 (접근 가능성이 높은) 유저만 실시간으로 커밋 수 변경 추적 후 갱신
        // 최근 로그인이 이루어지지 않은 유저는 페이지 최초 로그인 시 갱신이 발생한다.
        Set<String> activeUsers = redisTemplate.keys("commit_active:*");
        log.info("🔍 Active User Count: {}", activeUsers.size());

        // 현재 접속 기록이 있는 유저, 커밋 기록이 있는 유저는 주기적으로 갱신
        for (String key : activeUsers) {
            String username = key.replace("commit_active:", "");

            String lastcmKey = "commit_lastCommitted:" + username; // active유저의 key
            String lastCommitted = redisTemplate.opsForValue().get(lastcmKey); // 마지막 커밋 시간

            System.out.println("username: "+username);
            System.out.println("user lastCommitted: "+lastCommitted);
            if(username!=null && lastCommitted!=null) processUserCommit(username);
        }
    }

    // 🔹 유저 커밋 검사 및 반영
    private void processUserCommit(String username) {
        // 유저가 접속한 동안 추가한 commit수를 확인.
        String key = "commit_active:" + username; // active유저의 key
        String lastcmKey = "commit_lastCommitted:" + username; // active유저의 key
        String currentCommit = redisTemplate.opsForValue().get(key); // 현재까지 확인한 커밋 개수
        String lastcommitted = redisTemplate.opsForValue().get(lastcmKey); // 마지막 커밋 시간
        long updateTotalCommit, newCommitCount;


        LocalDateTime lastCommittedTime;
        try {
            lastCommittedTime = LocalDateTime.parse(lastcommitted, DateTimeFormatter.ISO_DATE_TIME);
        } catch (DateTimeParseException e) {
            System.out.println("lastcommitted 값이 올바르지 않음: " + lastcommitted);
            lastCommittedTime = LocalDateTime.now().minusHours(1);
        }

        // 현재 커밋 개수 조회
        updateTotalCommit = totalCommitService.getUpdateCommits(
            username,
            lastCommittedTime,  // 🚀 Redis에 저장된 lastCommitted 기준으로 조회
            LocalDateTime.now()
        ).getTotalCommitContributions();
        System.out.println("커밋 개수 불러들이기 완료, 현재까지 업데이트 된 커밋 수 : "+updateTotalCommit);

        if(currentCommit.equals("0") && updateTotalCommit > 0){
            User user = userRepository.findByUsername(username).get();
            LocalDateTime now = LocalDateTime.now();
            //이번 기간에 처음으로 커밋 수가 갱신된 경우, 이 시간을 기점으로 commitCount를 계산한다.
            user.setLastCommitted(now);
            userRepository.save(user);

            String redisKey = "commit_update:" + username; // 변경 알림을 위한 변수
            redisTemplate.opsForValue().set(redisKey, String.valueOf(updateTotalCommit), 3, TimeUnit.HOURS);

            redisTemplate.opsForValue().set(lastcmKey, String.valueOf(now), 3, TimeUnit.HOURS);
        }

        //기존 커밋이 있고 커밋 수에 변화가 있는 경우 처리
        newCommitCount = updateTotalCommit - Long.parseLong(currentCommit); // 새로 추가된 커밋 수
        if(newCommitCount>0){
            String redisKey = "commit_update:" + username; // 변경 알림을 위한 변수
            redisTemplate.opsForValue().set(redisKey, String.valueOf(newCommitCount), 3, TimeUnit.HOURS);

            updateTotalCommit+=newCommitCount;
            redisTemplate.opsForValue().set(key, String.valueOf(updateTotalCommit), 3, TimeUnit.HOURS);
        }

        // FIXME: 차후 리팩토링 필요
        log.info("🔍 User: {}, LastCommitted: {}, New Commits: {}, Total Commits: {}", username, lastcommitted, newCommitCount, currentCommit);
    }
}
