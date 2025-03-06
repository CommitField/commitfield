package cmf.commitField.domain.commit.scheduler;

import cmf.commitField.domain.pet.service.PetService;
import cmf.commitField.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommitUpdateListener {
    private final UserService userService;
    private final PetService petService;
    private final CommitUpdateService commitUpdateService;

    @EventListener
    public void handleCommitUserUpdateEvent(CommitUpdateEvent event) {
        String username = event.getUsername();
        long commitCount = event.getNewCommitCount();

        System.out.println("유저 시즌 경험치 업데이트: " + event.getUsername());
        // 이벤트 처리 로직
        boolean levelUp = userService.getExpUser(username,commitCount);
        if(levelUp) commitUpdateService.updateUserTier(username);

        // 모든 작업이 끝났다면
        userService.updateUserCommitCount(username, commitCount);
        // 커밋 갱신 후에 다른 서비스에서 필요한 작업 수행 (예: DB 업데이트, 상태 갱신 등)
        System.out.println("유저명: " + username + " has updated " + commitCount + " commits.");
    }

    @EventListener
    public void handleCommitPetUpdateEvent(CommitUpdateEvent event) {
        String username = event.getUsername();
        long commitCount = event.getNewCommitCount();

        System.out.println("유저 펫 경험치 업데이트: " + event.getUsername());
        // 이벤트 처리 로직
        petService.getExpPet(username,commitCount);

        // 커밋 갱신 후에 다른 서비스에서 필요한 작업 수행 (예: DB 업데이트, 상태 갱신 등)
        System.out.println("유저명: " + username + "'s pet has updated " + commitCount + " commits.");
    }
}
