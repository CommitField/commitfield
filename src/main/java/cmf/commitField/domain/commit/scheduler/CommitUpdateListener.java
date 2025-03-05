package cmf.commitField.domain.commit.scheduler;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class CommitUpdateListener {

    @EventListener
    public void handleCommitUpdateEvent(CommitUpdateEvent event) {
        // 이벤트 처리 로직
        Long userId = event.getUserId();
        int commitCount = event.getCommitCount();

        // 커밋 갱신 후에 다른 서비스에서 필요한 작업 수행 (예: DB 업데이트, 상태 갱신 등)
        System.out.println("User ID: " + userId + " has updated " + commitCount + " commits.");
    }
}
