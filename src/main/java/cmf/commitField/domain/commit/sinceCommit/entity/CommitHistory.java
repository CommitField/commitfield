package cmf.commitField.domain.commit.sinceCommit.entity;

import cmf.commitField.global.jpa.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "commit_history")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommitHistory extends BaseEntity {

    @Column(nullable = false)
    private String username; // GitHub 사용자명

    @Column(nullable = false)
    private int streak; // 연속 커밋 수

    @Column(nullable = false)
    private LocalDate commitDate; // 커밋한 날짜
}

