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
        log.info("Redis Template: {}", redisTemplate);
        String key = "commit:" + username; // Redis 키 생성 (ex: commit:hongildong)
        String value = redisTemplate.opsForValue().get(key); // Redis에서 값 가져오기
        return value != null ? Integer.parseInt(value) : null; // 값이 있으면 정수 변환, 없으면 null 반환
    }

    public void updateCachedCommitCount(String username, int count) {
        String key = "commit:" + username;
        redisTemplate.opsForValue().set(key, String.valueOf(count), Duration.ofHours(1)); // 1시간 캐싱
    }
}
