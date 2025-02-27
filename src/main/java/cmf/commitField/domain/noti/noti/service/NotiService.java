package cmf.commitField.domain.noti.noti.service;

import cmf.commitField.domain.noti.noti.repository.NotiRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class NotiService {
    private final NotiRepository notiRepository;

    public void sendCommitStreakNotification(String username, int streakCount) {
        log.info("🎉 {}님의 연속 커밋이 {}일로 증가했습니다!", username, streakCount);
        // 알림을 DB 저장 또는 웹소켓 / 이메일 / 푸시 알림 전송 가능
    }

//    public CommitAnalysisResponseDto getCommitAnalysis(String owner, String repo, String username, LocalDateTime since, LocalDateTime until) {
//        List<SinceCommitResponseDto> commits = getSinceCommits(owner, repo, since, until);
//        StreakResult streakResult = calculateStreaks(commits);
//
//        // 연속 커밋 수 Redis 업데이트 및 알림
//        streakService.updateStreak(username, streakResult.currentStreak, streakResult.maxStreak);
//
//        return new CommitAnalysisResponseDto(commits, streakResult.currentStreak, streakResult.maxStreak);
//    }

}
