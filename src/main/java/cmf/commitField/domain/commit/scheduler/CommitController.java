package cmf.commitField.domain.commit.scheduler;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/commit")
public class CommitController {
    @GetMapping("/get")
    public void getUserCommits(){
        // lastCommitted에서부터 새로 들어온 커밋 수를 검사해 반환한다
        // commitCount에서 새로 추가된 만큼의 커밋 수를 반환함
        // get이 실행되면 redis의 Active유저에 상태를 추가한다
        //
    }

    public void updateUserCommitStatus(){
        // 커밋 수에 변화가 있을 때 실행되는 메서드
        // 연속 커밋 반영, 펫 반영, 랭크 반영
    }

}
