package cmf.commitField.domain.user.service;

import cmf.commitField.domain.user.entity.MatchSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MatchService {
    private final String MATCH_QUEUE_KEY = "commit_match_queue";
    private final RedisTemplate<String, String> redisTemplate;
    private final SimpMessagingTemplate messagingTemplate;

    public void enqueueUser(String userId, int matchDuration) {
        redisTemplate.opsForList().rightPush(MATCH_QUEUE_KEY, userId + ":" + matchDuration);
        checkAndMatch();
    }

    private void checkAndMatch() {
        List<String> queue = redisTemplate.opsForList().range(MATCH_QUEUE_KEY, 0, -1);
        if (queue != null && queue.size() >= 2) {
            String player1 = queue.get(0);
            String player2 = queue.get(1);

            // 두 유저를 매칭 후 제거
            redisTemplate.opsForList().leftPop(MATCH_QUEUE_KEY);
            redisTemplate.opsForList().leftPop(MATCH_QUEUE_KEY);

            startMatch(player1, player2);
        }
    }

    private void startMatch(String player1, String player2) {
        String matchId = UUID.randomUUID().toString();
        int matchDuration = Integer.parseInt(player1.split(":")[1]); // 경쟁 시간 가져오기

        MatchSession matchSession = new MatchSession(matchId, player1, player2, matchDuration);
        redisTemplate.opsForHash().put("active_matches", matchId, matchSession);

        messagingTemplate.convertAndSend("/topic/match", "매칭 완료: " + player1 + " vs " + player2);
    }
}
