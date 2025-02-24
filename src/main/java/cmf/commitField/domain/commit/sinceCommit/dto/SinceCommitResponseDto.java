package cmf.commitField.domain.commit.sinceCommit.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SinceCommitResponseDto {
    private String sha;
    private Commit commit;
    private Author author;
    private String html_url;


    @Getter
    @NoArgsConstructor
    public static class Commit {
        private CommitAuthor author;  // 원래 코드 작성자
        private String message;

        @Getter
        @NoArgsConstructor
        public static class CommitAuthor {
            private String name;  // 커밋한 사람의 깃허브 이름
            private String email;
            private String date;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class Author {
        private String login;  // 커밋한 사람의 깃허브 id
        private String avatar_url;
        private String html_url;  // 사용자의 깃허브 주소
    }
}
