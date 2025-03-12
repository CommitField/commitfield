package cmf.commitField.domain.noti.noti.service;

import cmf.commitField.domain.noti.noti.entity.NotiDetailType;
import cmf.commitField.domain.noti.noti.entity.NotiType;
import cmf.commitField.domain.noti.noti.repository.NotiRepository;
import cmf.commitField.domain.user.entity.User;
import cmf.commitField.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommitAbsenceNotiService {
    private final UserRepository userRepository;
    private final NotiService notiService;
    private final NotiRepository notiRepository;

    // 매일 10시 실행
    @Scheduled(cron = "0 0 10 * * *")
    @Transactional
    public void sendCommitAbsenceNoti() {
        log.info("커밋 부재 알림 전송 시작");
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime thresholdDate = today.minusDays(10); // 10일 전 날짜 계산

        // 마지막 커밋이 10일 이상 지난 사용자 찾기
        List<User> inactiveUsers = userRepository.findUsersWithLastCommitBefore(thresholdDate);

        for (User user : inactiveUsers) {
            if (!hasRecentAbsenceNoti(user)) { // 최근 알림이 없는 경우에만 생성
                notiService.createStreakBrokenNoti(user);
                log.info("커밋 부재 알림 전송: {}", user.getUsername());
            }
        }
    }

    /**
     * 최근 10일 내 커밋 부재 알림이 있었는지 확인
     * @param user
     * @return
     */
    private boolean hasRecentAbsenceNoti(User user) {
        log.info("커밋 부재 알림 확인: {}", user.getUsername());
        LocalDateTime checkDate = LocalDateTime.now().minusDays(10);
        return notiRepository.existsByReceiverAndTypeCodeAndType2CodeAndCreatedAtAfter(
                user, NotiType.STREAK, NotiDetailType.STREAK_BROKEN, checkDate.toLocalDate().atTime(LocalTime.MIN)
        );
    }
}
