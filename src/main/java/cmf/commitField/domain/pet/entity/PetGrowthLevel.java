package cmf.commitField.domain.pet.entity;

import lombok.Getter;

@Getter
public enum PetGrowthLevel {
    LEVEL_1(150),
    LEVEL_2(300);

    private final int requiredExp;

    PetGrowthLevel(int requiredExp) {
        this.requiredExp = requiredExp;
    }

    public int getRequiredExp() {
        return requiredExp;
    }

    // 현재 경험치에 맞는 레벨 찾기
    public static PetGrowthLevel getLevelByExp(int exp) {
        PetGrowthLevel currentLevel = LEVEL_1;
        for (PetGrowthLevel level : values()) {
            if (exp >= level.getRequiredExp()) {
                currentLevel = level;
            }
        }
        return currentLevel;
    }
}