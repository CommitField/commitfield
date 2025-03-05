// java/cmf/commitField/global/scheduler/SeasonScheduler.java
package cmf.commitField.global.scheduler;

import cmf.commitField.domain.season.entity.Rank;
import cmf.commitField.domain.season.entity.Season;
import cmf.commitField.domain.season.entity.SeasonStatus;
import cmf.commitField.domain.season.entity.UserSeason;
import cmf.commitField.domain.season.repository.SeasonRepository;
import cmf.commitField.domain.season.repository.UserSeasonRepository;
import cmf.commitField.domain.season.service.SeasonService;
import cmf.commitField.domain.user.entity.User;
import cmf.commitField.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class SeasonScheduler {

    private final SeasonRepository seasonRepository;
    private final UserSeasonRepository userSeasonRepository;
    private final UserRepository userRepository;
    private final SeasonService seasonService;

//     매년 3, 6, 9, 12월 1일 자정마다 시즌 확인 및 생성
    @Scheduled(cron = "0 0 0 1 3,6,9,12 *")
    public void checkAndCreateNewSeason() {
        LocalDate today = LocalDate.now();
        String seasonName = today.getYear() + " " + getSeasonName(today.getMonthValue());

        // 현재 활성 시즌 조회
        Season activeSeason = seasonService.getActiveSeason();

        // 현재 활성 시즌이 없거나, 현재 시즌과 다르면 새로운 시즌 생성
        if (activeSeason == null || !activeSeason.getName().equals(seasonName)) {
            LocalDateTime startDate = getSeasonStartDate(today.getYear(), today.getMonth());
            LocalDateTime endDate = startDate.plusMonths(3).minusSeconds(1);

            // 현재 활성 시즌 종료
            if (activeSeason != null) {
                activeSeason.setStatus(SeasonStatus.INACTIVE);
                seasonRepository.save(activeSeason);
                log.info("✅ 기존 시즌 '{}' 종료됨", activeSeason.getName());
            }

            Season newSeason = seasonService.createNewSeason(seasonName, startDate, endDate);

            // 모든 유저의 랭크 초기화
            resetUserRanks(newSeason);

            // 새 시즌 시작 알림 저장 및 알림 전송
//            String message = notiService.createNewSeason(newSeason.getName());
//            notiWebSocketHandler.sendNotificationToAllUsers(message);
            System.out.println("새 시즌 생성: " + newSeason.getName());
        } else {
            System.out.println("이미 활성화된 시즌: " + activeSeason.getName());
        }
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

    // 월을 기준으로 시즌 이름 정하기 (예: 2025 Spring, 2025 Summer)
    private String getSeasonName(int month) {
        if (month >= 3 && month <= 5) {
            return "Spring"; // 봄
        } else if (month >= 6 && month <= 8) {
            return "Summer"; // 여름
        } else if (month >= 9 && month <= 11) {
            return "Fall"; // 가을
        } else {
            return "Winter"; // 겨울
        }
    }

    // 해당 시즌의 시작 날짜 반환
    private LocalDateTime getSeasonStartDate(int year, Month month) {
        int startMonth = switch (month) {
            case MARCH, APRIL, MAY -> 3;  // 봄
            case JUNE, JULY, AUGUST -> 6; // 여름
            case SEPTEMBER, OCTOBER, NOVEMBER -> 9; // 가을
            default -> 12; // 겨울 (12월부터 시작)
        };
        return LocalDateTime.of(year, startMonth, 1, 0, 0);
    }
}