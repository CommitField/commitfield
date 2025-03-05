package cmf.commitField.domain.noti.noti.service;

import cmf.commitField.domain.noti.noti.dto.NotiDto;
import cmf.commitField.domain.noti.noti.entity.Noti;
import cmf.commitField.domain.noti.noti.entity.NotiDetailType;
import cmf.commitField.domain.noti.noti.entity.NotiMessageTemplates;
import cmf.commitField.domain.noti.noti.entity.NotiType;
import cmf.commitField.domain.noti.noti.repository.NotiRepository;
import cmf.commitField.domain.season.entity.Season;
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

    // 알림 메시지 생성
    public static String generateMessage(NotiDetailType type, Object... params) {
        String template = NotiMessageTemplates.getTemplate(type);
        String message = MessageFormat.format(template, params);  // params 배열을 그대로 전달
        return message;
    }


    public List<NotiDto> getNotReadNoti(User receiver) {
        System.out.println("알림 조회");
        List<NotiDto> notis = notiRepository.findNotiDtoByReceiverId(receiver.getId(), false).orElse(null);
        System.out.println("알림 조회 끝");
        return notis;
    }

    public List<Noti> getSeasonNotiCheck(User receiver, long seasonId) {
        return notiRepository.findNotiByReceiverAndRelId(receiver, seasonId)
                .orElseThrow(() -> new CustomException(ErrorCode.ERROR_CHECK)); // 알림이 없을 경우 예외 발생
    }

    // 새 시즌 알림 생성
    @Transactional
    public void createNewSeason(Season season) {
        System.out.println("새 시즌 알림 생성");
        // 메시지 생성
        String message = NotiService.generateMessage(NotiDetailType.SEASON_START, season.getName());

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
        System.out.println("새 시즌 알림 생성 끝");
    }
}
