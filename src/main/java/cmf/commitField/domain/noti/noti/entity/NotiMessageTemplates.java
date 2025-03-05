package cmf.commitField.domain.noti.noti.entity;

import java.util.Map;

public class NotiMessageTemplates {
    // 알림 메시지 템플릿을 저장하는 맵
    private static final Map<NotiDetailType, String> TEMPLATES = Map.of(
            NotiDetailType.ACHIEVEMENT_COMPLETED, "🎉 {0}님이 [{1}] 업적을 달성했습니다!",
            NotiDetailType.STREAK_CONTINUED, "🔥 {0}님의 연속 커밋이 {1}일째 이어지고 있습니다!",
            NotiDetailType.STREAK_BROKEN, "😢 {0}님의 연속 커밋 기록이 끊겼습니다. 다음번엔 더 오래 유지해봐요!",
            NotiDetailType.SEASON_START, "🚀 새로운 [{0}] 시즌 이 시작되었습니다! 랭킹 경쟁을 준비하세요!"
    );

    // 알림 메시지 템플릿을 반환하는 메서드
    public static String getTemplate(NotiDetailType type) {
        return TEMPLATES.getOrDefault(type, "알림 메시지가 없습니다.");
    }
}
