package cmf.commitField.domain.redpanda.commit;

import cmf.commitField.domain.commit.sinceCommit.dto.CommitData;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Controller
public class WebhookCommitController {
    @MessageMapping("/topic/commits") // 클라이언트가 구독하는 topic
    public void handleCommitUpdate(@Payload CommitData commitData) {
        // commitData는 자동으로 JSON에서 변환된 객체입니다.
        System.out.println("User: " + commitData.getUser());
        System.out.println("Commits: " + commitData.getCommits());
    }
}
