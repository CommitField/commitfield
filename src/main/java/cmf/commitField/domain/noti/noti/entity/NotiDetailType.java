package cmf.commitField.domain.noti.noti.entity;

public enum NotiDetailType {
    // 업적
    RANK_UP, // 랭킹 상승
    ACHIEVEMENT_COMPLETED,  // 업적 달성

    // 연속
    STREAK_CONTINUED,  // 연속 커밋 이어짐
    STREAK_BROKEN,  // 연속 커밋 끊김

    NOTICE_CREATED, // 시즌
    SEASON_START    // 시즌 시작
}
