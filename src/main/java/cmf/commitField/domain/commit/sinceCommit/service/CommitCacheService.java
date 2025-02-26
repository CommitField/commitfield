package cmf.commitField.domain.commit.sinceCommit.service;

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
        String key = "commit:" + username;
        String value = redisTemplate.opsForValue().get(key);
        return value != null ? Integer.parseInt(value) : null;
    }

    public void updateCachedCommitCount(String username, int count) {
        String key = "commit:" + username;
        redisTemplate.opsForValue().set(key, String.valueOf(count), Duration.ofHours(1)); // 1시간 캐싱
    }
}
