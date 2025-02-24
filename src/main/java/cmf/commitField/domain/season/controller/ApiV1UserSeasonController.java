package cmf.commitField.domain.season.controller;

import cmf.commitField.domain.season.dto.UserSeasonDto;
import cmf.commitField.domain.season.entity.UserSeason;
import cmf.commitField.domain.season.service.UserSeasonService;
import cmf.commitField.global.globalDto.GlobalResponse;
import cmf.commitField.global.globalDto.GlobalResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user-season")
@RequiredArgsConstructor
public class ApiV1UserSeasonController {
    private final UserSeasonService userSeasonService;

    // 현재 시즌에 유저 랭크 추가하기 (SEED 등급)
    @GetMapping("/{userId}")
    public GlobalResponse<UserSeasonDto> addUserRank(@PathVariable Long userId) {
        UserSeason userSeason = userSeasonService.addUserRank(userId);
        UserSeasonDto userSeasonDto = new UserSeasonDto(userSeason);
        return GlobalResponse.success(userSeasonDto);
    }

    // 유저의 모든 시즌 랭크 조회
    @GetMapping("/{userId}/ranks")
    public GlobalResponse<List<UserSeasonDto>> getUserRanks(@PathVariable Long userId) {
        List<UserSeason> userSeasons = userSeasonService.getUserRanks(userId);

        // UserSeason -> UserSeasonDto 변환
        List<UserSeasonDto> userSeasonDtos = userSeasons.stream()
                .map(UserSeasonDto::new)
                .collect(Collectors.toList());

        return GlobalResponse.success(userSeasonDtos);
    }

    // 특정 시즌의 유저 랭크 조회
    @GetMapping("/{userId}/season/{seasonId}")
    public GlobalResponse<UserSeasonDto> getUserRankBySeason(
            @PathVariable Long userId,
            @PathVariable Long seasonId
    ) {
        UserSeason userSeason = userSeasonService.getUserRankBySeason(userId, seasonId);
        UserSeasonDto userSeasonDto = new UserSeasonDto(userSeason);
        return GlobalResponse.success(userSeasonDto);
    }
}

