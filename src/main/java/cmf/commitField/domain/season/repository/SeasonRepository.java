package cmf.commitField.domain.season.repository;

import cmf.commitField.domain.season.entity.Season;
import cmf.commitField.domain.season.entity.SeasonStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeasonRepository extends JpaRepository<Season, Long> {
    Season findByStatus(SeasonStatus status);
}
