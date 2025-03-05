package cmf.commitField.domain.noti.noti.service;

import cmf.commitField.domain.noti.noti.entity.Noti;
import cmf.commitField.domain.noti.noti.entity.NotiDetailType;
import cmf.commitField.domain.noti.noti.entity.NotiMessageTemplates;
import cmf.commitField.domain.noti.noti.entity.NotiType;
import cmf.commitField.domain.noti.noti.repository.NotiRepository;
import cmf.commitField.domain.season.entity.Season;
import cmf.commitField.domain.season.repository.SeasonRepository;
import cmf.commitField.domain.season.service.SeasonService;
import cmf.commitField.domain.user.entity.User;
import cmf.commitField.domain.user.repository.UserRepository;
import cmf.commitField.global.error.ErrorCode;
import cmf.commitField.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class NotiService {
    private final NotiRepository notiRepository;
    private final UserRepository userRepository;
    private final SeasonRepository seasonRepository;
    private final SeasonService seasonService;

    // 알림 메시지 생성
    public static String generateMessage(NotiDetailType type, Object... params) {
        String template = NotiMessageTemplates.getTemplate(type);
        log.info("generateMessage - params: {}", params);
        log.info("generateMessage - template: {}", template);  // template 자체를 출력
        String message = MessageFormat.format(template, params);  // params 배열을 그대로 전달
        log.info("generateMessage - message: {}", message);
        return message;
    }


    public List<Noti> getNotReadNoti(User receiver) {
        log.info("getNotReadNoti - receiver: {}", receiver);
        List<Noti> notis = notiRepository.findNotiByReceiverAndIsRead(receiver, false).orElse(null);
        log.info("getNotReadNoti - notis: {}", notis);
        return notis;
    }

    public List<Noti> getSeasonNotiCheck(User receiver, long seasonId) {
        log.info("getSeasonNotiCheck - receiver: {}, seasonId: {}", receiver, seasonId);
        return notiRepository.findNotiByReceiverAndRelId(receiver, seasonId)
                .orElseThrow(() -> new CustomException(ErrorCode.ERROR_CHECK)); // 알림이 없을 경우 예외 발생
    }

    // 새 시즌 알림 생성
    @Transactional
    public void createNewSeason(Season season) {
        log.info("createNewSeason - season: {}", season.getName());
        // 메시지 생성
        String message = NotiService.generateMessage(NotiDetailType.SEASON_START, season.getName());
        log.info("createNewSeason - message: {}", message);

        // 모든 사용자 조회
        Iterable<User> users = userRepository.findAll();

        // 모든 유저 알림 객체 생성
        users.forEach(user -> {
            Noti noti = Noti.builder()
                    .typeCode(NotiType.SEASON)
                    .type2Code(NotiDetailType.SEASON_START)
                    .receiver(user)
                    .isRead(false)
                    .message(message)
                    .relId(season.getId())
                    .relTypeCode(season.getModelName())
                    .build();

            notiRepository.save(noti);
        });
    }
}
