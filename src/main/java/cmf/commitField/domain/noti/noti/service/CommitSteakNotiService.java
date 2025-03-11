package cmf.commitField.domain.noti.noti.service;

import cmf.commitField.domain.commit.totalCommit.service.TotalCommitService;
import cmf.commitField.domain.noti.noti.entity.NotiDetailType;
import cmf.commitField.domain.noti.noti.entity.NotiType;
import cmf.commitField.domain.noti.noti.repository.NotiRepository;
import cmf.commitField.domain.user.entity.User;
import cmf.commitField.domain.user.repository.UserRepository;
import cmf.commitField.domain.user.service.UserService;
import cmf.commitField.global.error.ErrorCode;
import cmf.commitField.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommitSteakNotiService {
    private final UserRepository userRepository;
    private final TotalCommitService totalCommitService;
    private final NotiService notiService;
    private final NotiRepository notiRepository;

    // 매일 10시 실행
    @Scheduled(cron = "0 0 10 * * *")
    @Transactional
    public void sendCommitSteakNoti() {
        List<User> users = userRepository.findAll();

        for (User user : users) {
            int currentStreakCommit = totalCommitService.getTotalCommitCount(user.getUsername()).getCurrentStreakDays();
            checkAndCreateSteakNoti(user, currentStreakCommit);
        }
    }

    public void checkAndCreateSteakNoti(User user, int currentStreakCommit) {
        boolean alreadyNotified = notiRepository.existsByReceiverAndType2CodeAndCreatedAtAfter(
                user, NotiDetailType.STREAK_CONTINUED, LocalDate.now().atStartOfDay()
        );

        log.info("알림 상세 타입: {}", NotiDetailType.STREAK_CONTINUED.name());

        if (shouldNotify(currentStreakCommit) && !alreadyNotified) {
            log.info("🔍 연속 커밋 축하 알림 User: {}, Streak: {}", user.getUsername(), currentStreakCommit);
            notiService.createStreakCommitNoti(user, String.valueOf(currentStreakCommit));
        }
    }


    /**
     * 특정 연속 커밋 횟수에 도달했는지 확인
     */
    public boolean shouldNotify(int streak) {
        return streak == 3 || (streak % 10 == 0);
    }
}
