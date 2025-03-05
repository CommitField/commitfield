package cmf.commitField.domain.pet.entity;

import lombok.Getter;

@Getter
public enum PetGrow {
    EGG(0),
    HATCH(150),
    GROWN(300);

    private final int requiredExp;

    PetGrow(int requiredExp) {
        this.requiredExp = requiredExp;
    }

    public int getRequiredExp() {
        return requiredExp;
    }

    // 현재 경험치에 맞는 레벨 찾기
    public static PetGrow getLevelByExp(int exp) {
        PetGrow currentLevel = EGG;
        for (PetGrow level : values()) {
            if (exp >= level.getRequiredExp()) {
                currentLevel = level;
            }
        }
        return currentLevel;
    }
}