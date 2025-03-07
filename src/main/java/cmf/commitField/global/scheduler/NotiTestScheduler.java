package cmf.commitField.global.scheduler;

import cmf.commitField.domain.noti.noti.service.NotiService;
import cmf.commitField.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotiTestScheduler {
    private final NotiService notiService;
    private final UserRepository userRepository;

//    @Scheduled(cron = "0 44 * * * *")
//    public void test() {
//        System.out.println("test 실행");
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        if (authentication instanceof OAuth2AuthenticationToken) {
//            OAuth2User principal = (OAuth2User) authentication.getPrincipal();
//            Map<String, Object> attributes = principal.getAttributes();
//            String username = (String) attributes.get("login");  // GitHub ID
//            User user = userRepository.findByUsername(username).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
//            notiService.createNoti(user);
//        }
//
//    }
}
