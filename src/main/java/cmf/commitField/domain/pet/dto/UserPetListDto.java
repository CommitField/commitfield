package cmf.commitField.domain.pet.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserPetListDto {
    private String username;
    private Map<Integer, Integer> petList; // 아이디, 소유 갯수
}