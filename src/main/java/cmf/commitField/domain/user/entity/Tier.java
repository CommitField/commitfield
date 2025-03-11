package cmf.commitField.domain.user.entity;

public enum Tier {
    NONE(0), // 미획득(하나도 Commit하지 않음)
    SEED(1),      // 씨앗
    SPROUT(95),    // 새싹
    FLOWER(189),    // 꽃
    FRUIT(283),     // 열매
    TREE(377);       // 나무

    private final long requiredExp;

    Tier(long requiredExp) {
        this.requiredExp = requiredExp;
    }

    public long getRequiredExp() {
        return requiredExp;
    }

    // 현재 경험치에 맞는 레벨 찾기
    public static Tier getLevelByExp(long exp) {
        Tier currentLevel = SEED;
        for (Tier level : values()) {
            if (exp >= level.getRequiredExp()) {
                currentLevel = level;
            }
        }
        return currentLevel;
    }
}