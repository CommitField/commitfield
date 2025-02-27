package cmf.commitField.domain.noti.streak.service;

import cmf.commitField.domain.noti.noti.service.NotiService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StreakService {
    private final RedisTemplate<String, String> redisTemplate;

    private final NotiService notiService;

    public void updateStreak(String username, int newCurrentStreak, int newMaxStreak) {
        String currentStreakKey = "user:" + username + ":current_streak";
        String maxStreakKey = "user:" + username + ":max_streak";

        int prevCurrentStreak = getStreak(currentStreakKey);
        int prevMaxStreak = getStreak(maxStreakKey);

        // 연속 커밋이 증가했으면 Redis 업데이트 및 알림 발송
        if (newCurrentStreak > prevCurrentStreak) {
            // redis 업데이트
            redisTemplate.opsForValue().set(currentStreakKey, String.valueOf(newCurrentStreak));

            // 알림 발송
//            notiService.sendCommitStreakNotification(username, newCurrentStreak);
        }
    }

    private int getStreak(String key) {
        String value = redisTemplate.opsForValue().get(key);
        return (value != null) ? Integer.parseInt(value) : 0;
    }
}
