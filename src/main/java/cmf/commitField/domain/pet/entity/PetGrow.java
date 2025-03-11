package cmf.commitField.domain.pet.entity;

import lombok.Getter;

@Getter
public enum PetGrow {
    //FIXME: 테스트를 위한 수정, 차후 150/300으로 변경 필요
    EGG(0),
    HATCH(10),
    GROWN(15);

    private final int requiredExp;

    PetGrow(int requiredExp) {
        this.requiredExp = requiredExp;
    }

    public int getRequiredExp() {
        return requiredExp;
    }

    // 현재 경험치에 맞는 레벨 찾기
    public static PetGrow getLevelByExp(long exp) {
        PetGrow currentLevel = EGG;
        for (PetGrow level : values()) {
            if (exp >= level.getRequiredExp()) {
                currentLevel = level;
            }
        }
        return currentLevel;
    }
}