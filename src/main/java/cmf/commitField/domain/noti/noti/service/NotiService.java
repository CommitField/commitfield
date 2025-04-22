package cmf.commitField.domain.noti.noti.service;

import cmf.commitField.domain.noti.noti.dto.NotiDto;
import cmf.commitField.domain.noti.noti.entity.Noti;
import cmf.commitField.domain.noti.noti.entity.NotiDetailType;
import cmf.commitField.domain.noti.noti.entity.NotiType;
import cmf.commitField.domain.noti.noti.event.NotiEvent;
import cmf.commitField.domain.noti.noti.repository.NotiRepository;
import cmf.commitField.domain.season.entity.Season;
import cmf.commitField.domain.user.entity.User;
import cmf.commitField.global.error.ErrorCode;
import cmf.commitField.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class NotiService {
    private final NotiRepository notiRepository;
    private final ApplicationEventPublisher eventPublisher;

    /**
     * 알림 생성 공통 로직입니다.
     * 받아온 params를 이용하여 메시지를 생성하고 알림 엔티티를 생성합니다.
     * 메시지에 필요한 파라미터는 NotiDetailType에 정의된 순서대로 전달해야 합니다.
     * 만약 NotiDetailType에 정의된 파라미터가 없는 경우에는 빈 배열을 전달해야 합니다.
     * 데이터베이스에 알림 엔티티를 저장하고 WebSocket 이벤트를 발생시킵니다.
     * @param receiver
     * @param notiType
     * @param notiDetailType
     * @param relId
     * @param relTypeCode
     * @param params
     */
    @Transactional
    public void createNoti(User receiver, NotiType notiType, NotiDetailType notiDetailType, Long relId, String relTypeCode, Object... params) {
        // 메시지 생성
        String message = notiDetailType.formatMessage(params);

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

    /**
     * 알림 조회
     * @param receiver
     * @return
     */
    public List<NotiDto> getNotReadNoti(User receiver) {
        List<NotiDto> notis = notiRepository.findNotiDtoByReceiverId(receiver.getId(), false).orElse(null);
        return notis;
    }

    /**
     * 시즌 알림 확인
     * 알림이 없을 경우 예외 발생
     * @param receiver
     * @param seasonId
     * @return List
     */
    public List<Noti> getSeasonNotiCheck(User receiver, long seasonId) {
        return notiRepository.findNotiByReceiverAndRelId(receiver, seasonId)
                .orElseThrow(() -> new CustomException(ErrorCode.ERROR_CHECK)); // 알림이 없을 경우 예외 발생
    }

    /**
     * 새 시즌 알림 생성
     * @param season
     * @param user
     */
    @Transactional
    public void createNewSeasonNoti(Season season, User user) {
        createNoti(user, NotiType.SEASON, NotiDetailType.SEASON_START, season.getId(), season.getModelName(), season.getName());
    }

    /**
     * 랭킹 업 알림 생성
     * @param user
     */
    @Transactional
    public void createRankUpNoti(User user) {
        createNoti(user, NotiType.RANK, NotiDetailType.RANK_UP, 0L, null, getDisplayName(user), user.getTier().name());
    }

    /**
     * 연속 커밋 축하 알림 생성
     * @param user
     * @param days
     */
    @Transactional
    public void createStreakCommitNoti(User user, String days) {
        createNoti(user, NotiType.STREAK, NotiDetailType.STREAK_CONTINUED, 0L, null, getDisplayName(user), days);
    }

    /**
     * 커밋 부재 알림 생성
     * @param user
     */
    @Transactional
    public void createStreakBrokenNoti(User user) {
        createNoti(user, NotiType.STREAK, NotiDetailType.STREAK_BROKEN, 0L, null, getDisplayName(user));
    }

    /**
     * 업적 알림 생성
     * @param user
     * @param achievementName
     */
    // TODO: 업적 로직 구현 후 사용
    @Transactional
    public void createAchievementNoti(User user, String achievementName) {
        createNoti(user, NotiType.ACHIEVEMENT, NotiDetailType.ACHIEVEMENT_COMPLETED, 0L, null, getDisplayName(user), achievementName);
    }

    /**
     * 공지사항 알림 생성
     * @param user
     * @param noticeTitle
     */
    // TODO: 공지사항 로직 구현 후 사용
    @Transactional
    public void createNoticeNoti(User user, String noticeTitle) {
        createNoti(user, NotiType.NOTICE, NotiDetailType.NOTICE_CREATED, 0L, null, noticeTitle);
    }

    /**
     * 알림 읽음 처리:
     * 알림 버튼 클릭 시 모든 알림이 읽음 상태로 변경되기 때문에
     * 사용자의 모든 알림의 상태를 true로 변경하는 메서드
     * @param receiver
     */
    @Transactional
    public void read(User receiver) {
        log.info("✅ read notis");
        List<Noti> notis = notiRepository.findNotiByReceiver(receiver).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
        notis.forEach(noti -> {
            noti.setRead(true);
        });
    }

    /**
     * 닉네임 여부에 따라 표시 이름 반환:
     * 닉네임이 있으면 닉네임, 없으면 유저네임 반환
     * @param user
     * @return
     */
    private String getDisplayName(User user) {
        return user.getNickname() != null ? user.getNickname() : user.getUsername();
    }
}