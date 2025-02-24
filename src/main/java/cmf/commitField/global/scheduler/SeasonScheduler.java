package cmf.commitField.global.scheduler;

import cmf.commitField.domain.season.entity.Rank;
import cmf.commitField.domain.season.entity.Season;
import cmf.commitField.domain.season.entity.SeasonStatus;
import cmf.commitField.domain.season.entity.UserSeason;
import cmf.commitField.domain.season.repository.SeasonRepository;
import cmf.commitField.domain.season.repository.UserSeasonRepository;
import cmf.commitField.domain.user.entity.User;
import cmf.commitField.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class SeasonScheduler {

    private final SeasonRepository seasonRepository;
    private final UserSeasonRepository userSeasonRepository;
    private final UserRepository userRepository;

    @Scheduled(cron = "0 0 0 1 1,4,7,10 *")  // 1월, 4월, 7월, 10월 1일 00:00:00에 실행
    @Transactional
    public void resetSeason() {
        log.info("🕒 시즌 초기화 스케줄러 실행");

        // 현재 활성화된 시즌 종료
        Season currentSeason = seasonRepository.findByStatus(SeasonStatus.ACTIVE);
        if (currentSeason != null) {
            currentSeason.setStatus(SeasonStatus.INACTIVE);
            seasonRepository.save(currentSeason);
            log.info("✅ 기존 시즌 '{}' 종료됨", currentSeason.getName());
        }

        // 새로운 시즌 생성
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = now.withDayOfMonth(1);
        LocalDateTime end = start.plusMonths(3).minusSeconds(1);

        Season newSeason = Season.builder()
                .name("Season " + start.getYear() + " Q" + ((start.getMonthValue() - 1) / 3 + 1))
                .startDate(start)
                .endDate(end)
                .status(SeasonStatus.ACTIVE)
                .build();

        seasonRepository.save(newSeason);
        log.info("🌟 새로운 시즌 '{}' 생성됨 (기간: {} ~ {})", newSeason.getName(), start, end);

        // 모든 유저의 랭크 초기화
        resetUserRanks(newSeason);
    }

    private void resetUserRanks(Season newSeason) {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            UserSeason userSeason = UserSeason.builder()
                    .user(user)
                    .season(newSeason)
                    .rank(Rank.SEED)  // 초기 랭크 설정 (예: SEED)
                    .build();

            userSeasonRepository.save(userSeason);
            log.info("🔄 유저 '{}'의 시즌 랭크 초기화 ({} -> 씨앗)", user.getNickname(), newSeason.getName());
        }
    }
}
