package cmf.commitField.domain.season.service;

import cmf.commitField.domain.season.entity.Season;
import cmf.commitField.domain.season.entity.SeasonStatus;
import cmf.commitField.domain.season.repository.SeasonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SeasonService {
    private final SeasonRepository seasonRepository;

    public Season createNewSeason(String name, LocalDateTime start, LocalDateTime end) {
        Season season = Season.builder()
                .name(name)
                .startDate(start)
                .endDate(end)
                .status(SeasonStatus.ACTIVE)
                .build();
        return seasonRepository.save(season);
    }

    public Season getActiveSeason() {
        return seasonRepository.findByStatus(SeasonStatus.ACTIVE);
    }
}
