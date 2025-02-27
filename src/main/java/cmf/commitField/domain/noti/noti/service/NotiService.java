package cmf.commitField.domain.noti.noti.service;

import cmf.commitField.domain.noti.noti.entity.Noti;
import cmf.commitField.domain.noti.noti.entity.NotiDetailType;
import cmf.commitField.domain.noti.noti.entity.NotiMessageTemplates;
import cmf.commitField.domain.noti.noti.entity.NotiType;
import cmf.commitField.domain.noti.noti.repository.NotiRepository;
import cmf.commitField.domain.user.entity.User;
import cmf.commitField.domain.user.repository.UserRepository;
import cmf.commitField.global.error.ErrorCode;
import cmf.commitField.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class NotiService {
    private final NotiRepository notiRepository;
    private final UserRepository userRepository;

    // 알림 메시지 생성
    public static String generateMessage(NotiDetailType type, Object... params) {
        String template = NotiMessageTemplates.getTemplate(type);
        return MessageFormat.format(template, params);
    }

    // 연속 커밋 알림 생성
    @Transactional
    public Noti createCommitStreak(String username, NotiType type, NotiDetailType detailType, Object... params) {
        // 메시지 생성
        String message = NotiService.generateMessage(detailType, params);

        // 사용자 조회 (없으면 예외 처리)
        User user = userRepository.findByUsername(username).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        // 알림 객체 생성 후 저장
        Noti noti = Noti.builder()
                .typeCode(type)
                .type2Code(detailType)
                .receiver(user)
                .isRead(false)
                .message(message)
                .build();

        return notiRepository.save(noti);
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
