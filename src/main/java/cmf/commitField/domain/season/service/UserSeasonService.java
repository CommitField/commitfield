package cmf.commitField.domain.season.service;

import cmf.commitField.domain.season.entity.Rank;
import cmf.commitField.domain.season.entity.Season;
import cmf.commitField.domain.season.entity.UserSeason;
import cmf.commitField.domain.season.repository.SeasonRepository;
import cmf.commitField.domain.season.repository.UserSeasonRepository;
import cmf.commitField.domain.user.entity.User;
import cmf.commitField.domain.user.repository.UserRepository;
import cmf.commitField.global.error.ErrorCode;
import cmf.commitField.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserSeasonService {
    private final UserSeasonRepository userSeasonRepository;
    private final UserRepository userRepository;
    private final SeasonRepository seasonRepository;
    private final SeasonService seasonService;

    // 현재 시즌에 유저 랭크 추가하기 (SEED 등급)
    public UserSeason addUserRank(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        Season season = seasonService.getActiveSeason();

        UserSeason userSeason = new UserSeason();
        userSeason.setUser(user);
        userSeason.setSeason(season);
        userSeason.setRank(Rank.SEED);

        return userSeasonRepository.save(userSeason);
    }

    // 유저의 모든 시즌 랭크 조회
    public List<UserSeason> getUserRanks(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
        return userSeasonRepository.findByUser(user);
    }

    // 특정 시즌의 유저 랭크 조회
    public UserSeason getUserRankBySeason(Long userId, Long seasonId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
        Season season = seasonRepository.findById(seasonId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_SEASON));
        return userSeasonRepository.findByUserAndSeason(user, season)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_SEASON_RANK));
    }
}
