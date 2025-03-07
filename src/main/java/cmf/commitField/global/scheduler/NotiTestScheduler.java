package cmf.commitField.global.scheduler;

import cmf.commitField.domain.noti.noti.service.NotiService;
import cmf.commitField.domain.user.entity.User;
import cmf.commitField.domain.user.repository.UserRepository;
import cmf.commitField.global.error.ErrorCode;
import cmf.commitField.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotiTestScheduler {
    private final NotiService notiService;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Scheduled(cron = "30 14 * * * *")
    public void test() {
        System.out.println("test 실행");

        User user = userRepository.findById(1L).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
        notiService.createNoti(user);
//        eventPublisher.publishEvent();
        }
}
