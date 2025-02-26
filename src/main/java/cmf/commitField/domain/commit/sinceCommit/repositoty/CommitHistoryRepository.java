package cmf.commitField.domain.commit.sinceCommit.repositoty;

import cmf.commitField.domain.commit.sinceCommit.entity.CommitHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommitHistoryRepository extends JpaRepository<CommitHistory, Long> {
    // 특정 유저의 최신 커밋 기록 조회
    Optional<CommitHistory> findTopByUsernameOrderByCommitDateDesc(String username);
}
