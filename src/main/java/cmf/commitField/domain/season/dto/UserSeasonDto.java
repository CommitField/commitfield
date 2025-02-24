package cmf.commitField.domain.season.dto;

import cmf.commitField.domain.season.entity.Rank;
import cmf.commitField.domain.season.entity.UserSeason;
import lombok.Getter;

@Getter
public class UserSeasonDto {
    private Long userId;
    private String username;
    private String seasonName;
    private Rank rank;

    public UserSeasonDto(UserSeason userSeason) {
        this.userId = userSeason.getUser().getId();
        this.username = userSeason.getUser().getUsername();
        this.seasonName = userSeason.getSeason().getName();
        this.rank = userSeason.getRank();
    }
}