package cmf.commitField.domain.season.entity;

import lombok.Getter;

@Getter
public enum Rank {
    SEED(94),      // 씨앗
    SPROUT(188),    // 새싹
    FLOWER(282),    // 꽃
    FRUIT(375),     // 열매
    TREE(376);       // 나무

    private final int requiredExp;

    Rank(int requiredExp) {
        this.requiredExp = requiredExp;
    }

    public int getRequiredExp() {
        return requiredExp;
    }

    // 현재 경험치에 맞는 레벨 찾기
    public static Rank getLevelByExp(int exp) {
        Rank currentLevel = SEED;
        for (Rank level : values()) {
            if (exp >= level.getRequiredExp()) {
                currentLevel = level;
            }
        }
        return currentLevel;
    }
}