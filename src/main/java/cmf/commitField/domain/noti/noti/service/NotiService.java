package cmf.commitField.domain.noti.noti.service;

import cmf.commitField.domain.noti.noti.dto.NotiDto;
import cmf.commitField.domain.noti.noti.entity.Noti;
import cmf.commitField.domain.noti.noti.entity.NotiDetailType;
import cmf.commitField.domain.noti.noti.entity.NotiMessageTemplates;
import cmf.commitField.domain.noti.noti.entity.NotiType;
import cmf.commitField.domain.noti.noti.event.NotiEvent;
import cmf.commitField.domain.noti.noti.repository.NotiRepository;
import cmf.commitField.domain.season.entity.Rank;
import cmf.commitField.domain.season.entity.Season;
import cmf.commitField.domain.user.entity.User;
import cmf.commitField.domain.user.repository.UserRepository;
import cmf.commitField.global.error.ErrorCode;
import cmf.commitField.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class NotiService {
    private final NotiRepository notiRepository;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    // 알림 메시지 생성
    public static String generateMessage(NotiDetailType type, Object... params) {
        String template = NotiMessageTemplates.getTemplate(type);
        String message = MessageFormat.format(template, params);  // params 배열을 그대로 전달
        return message;
    }

    // 알림 생성
    @Transactional
    public void createNoti(User receiver, NotiType notiType, NotiDetailType notiDetailType, Long relId, String relTypeCode, Object... params) {
        // 메시지 생성
        String message = NotiService.generateMessage(notiDetailType, params);

        // 알림 엔티티 생성
        Noti noti = Noti.builder()
                .typeCode(notiType)
                .type2Code(notiDetailType)
                .receiver(receiver)
                .isRead(false)
                .message(message)
                .relId(relId)
                .relTypeCode(relTypeCode)
                .build();

        notiRepository.save(noti);

        // WebSocket 이벤트 발생
        List<NotiDto> notis = new ArrayList<>();
        notis.add(new NotiDto(noti.getId(), noti.getMessage(), noti.getCreatedAt()));
        NotiEvent event = new NotiEvent(this, receiver.getUsername(), notis, "새로운 알림이 생성되었습니다.");
        eventPublisher.publishEvent(event);
    }

    // 알림 조회
    public List<NotiDto> getNotReadNoti(User receiver) {
        List<NotiDto> notis = notiRepository.findNotiDtoByReceiverId(receiver.getId(), false).orElse(null);
        return notis;
    }

    // 시즌 알림 확인
    public List<Noti> getSeasonNotiCheck(User receiver, long seasonId) {
        return notiRepository.findNotiByReceiverAndRelId(receiver, seasonId)
                .orElseThrow(() -> new CustomException(ErrorCode.ERROR_CHECK)); // 알림이 없을 경우 예외 발생
    }

    // 새 시즌 알림 생성
    @Transactional
    public void createNewSeasonNoti(Season season, User user) {
        createNoti(user, NotiType.SEASON, NotiDetailType.SEASON_START, season.getId(), season.getModelName(), season.getName());
    }

    // 랭킹 업 알림 생성
    @Transactional
    public void createRankUpNoti(User user) {
        createNoti(user, NotiType.RANK, NotiDetailType.RANK_UP, 0L, null, getDisplayName(user), user.getTier().name());
    }

    // 연속 커밋 축하 알림 생성
    @Transactional
    public void createStreakCommitNoti(User user, String days) {
        createNoti(user, NotiType.STREAK, NotiDetailType.STREAK_CONTINUED, 0L, null, getDisplayName(user), days);
    }

    // 커밋 부재 알림 생성
    @Transactional
    public void createStreakBrokenNoti(User user) {
        createNoti(user, NotiType.STREAK, NotiDetailType.STREAK_BROKEN, 0L, null, getDisplayName(user));
    }

    // 업적 알림 생성
    @Transactional
    public void createAchievementNoti(User user, String achievementName) {
        createNoti(user, NotiType.ACHIEVEMENT, NotiDetailType.ACHIEVEMENT_COMPLETED, 0L, null, getDisplayName(user), achievementName);
    }

    // 공지사항 알림 생성
    @Transactional
    public void createNoticeNoti(User user, String noticeTitle) {
        createNoti(user, NotiType.NOTICE, NotiDetailType.NOTICE_CREATED, 0L, null, noticeTitle);
    }

    // 읽음 처리
    @Transactional
    public List<Noti> read(User receiver) {
        System.out.println("알림 읽음 처리");
        List<Noti> notis = notiRepository.findNotiByReceiver(receiver).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
        notis.forEach(noti -> {
            noti.setRead(true);
        });
        System.out.println("알림 읽음 처리 끝");
        return notis;
    }

    private String getDisplayName(User user) {
        return user.getNickname() != null ? user.getNickname() : user.getUsername();
    }
}