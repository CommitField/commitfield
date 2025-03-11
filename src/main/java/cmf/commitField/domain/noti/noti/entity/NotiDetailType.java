package cmf.commitField.domain.noti.noti.entity;

import java.text.MessageFormat;
import java.util.Arrays;

public enum NotiDetailType {
    ACHIEVEMENT_COMPLETED("🎉 {0}님이 [{1}] 업적을 달성했습니다!", new String[]{"nickname", "achievementName"}),
    STREAK_CONTINUED("🔥 {0}님의 연속 커밋이 {1}일째 이어지고 있습니다!", new String[]{"nickname", "days"}),
    STREAK_BROKEN("😢 {0}님의 연속 커밋 기록이 끊겼습니다.", new String[]{"nickname"}),
    SEASON_START("🚀 새로운 [{0}] 시즌이 시작되었습니다! 랭킹 경쟁을 준비하세요!", new String[]{"seasonName"}),
    RANK_UP("📈 축하합니다! {0}님의 랭킹이 {1}(으)로 상승했습니다! 🎊", new String[]{"nickname", "tier"}),
    NOTICE_CREATED("📢 공지사항이 있습니다: {0}", new String[]{"noticeTitle"});

    private final String template;
    private final String[] paramNames;

    NotiDetailType(String template, String[] paramNames) {
        this.template = template;
        this.paramNames = paramNames;
    }

    public String getTemplate() {
        return template;
    }

    public String[] getParamNames() {
        return paramNames;
    }

    public String formatMessage(Object... params) {
        if (paramNames.length != params.length) {
            throw new IllegalArgumentException("🚨 잘못된 파라미터 개수! 필요: " +
                    Arrays.toString(paramNames) + ", 제공됨: " + Arrays.toString(params));
        }
        return MessageFormat.format(template, params);
    }
}


