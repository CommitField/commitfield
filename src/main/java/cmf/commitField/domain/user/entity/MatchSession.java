package cmf.commitField.domain.user.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MatchSession {
    private String matchId;
    private String player1;
    private String player2;
    private int matchDuration; // 1/3/5시간
    private long startTime;
    private int player1Commits;
    private int player2Commits;

    public MatchSession(String matchId, String player1, String player2, int matchDuration) {
        this.matchId = matchId;
        this.player1 = player1;
        this.player2 = player2;
        this.matchDuration = matchDuration;
        this.startTime = System.currentTimeMillis();
        this.player1Commits = 0;
        this.player2Commits = 0;
    }

    public boolean isTimeUp() {
        return System.currentTimeMillis() - startTime >= matchDuration * 3600000;
    }
}
