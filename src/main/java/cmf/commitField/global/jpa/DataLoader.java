package cmf.commitField.global.jpa;

import cmf.commitField.global.scheduler.SeasonScheduler;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class DataLoader {

    private final SeasonScheduler seasonScheduler;

    // 애플리케이션 시작 시 자동 실행되는 메서드
    @Bean
    public ApplicationRunner loadInitialData() {
        return args -> {
            // DataLoader에서 바로 SeasonScheduler의 메서드를 호출
            seasonScheduler.checkAndCreateNewSeason();
        };
    }
}