package cmf.commitField.domain.commit.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommitCacheService {
    private final StringRedisTemplate redisTemplate;

    public Integer getCachedCommitCount(String username) {
        String key = "commit_active:" + username; // Redis 키 생성
        String value = redisTemplate.opsForValue().get(key); // Redis에서 값 가져오기

        if (value != null) {
            log.info("✅ Redis Hit - {} : {}", key, value);
            return Integer.parseInt(value);
        } else {
            log.info("❌ Redis Miss - {}", key);
            return null;
        }
    }

    public void updateCachedCommitCount(String username, long count) {
        String key = "commit_active:" + username;
        redisTemplate.opsForValue().set(key, String.valueOf(count), Duration.ofHours(3)); // 3시간 캐싱
    }
}
