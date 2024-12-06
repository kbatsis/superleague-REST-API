package com.project.superleague.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
public class MatchReadOnlyDTO extends BaseDTO {
    private LocalDate matchDate;

    private Integer goalsHost;

    private Integer goalsGuest;

    private Long hostTeamId;

    private Long guestTeamId;

    public MatchReadOnlyDTO(Long id, LocalDate matchDate, Integer goalsHost, Integer goalsGuest, Long hostTeamId, Long guestTeamId) {
        setId(id);
        this.matchDate = matchDate;
        this.goalsHost = goalsHost;
        this.goalsGuest = goalsGuest;
        this.hostTeamId = hostTeamId;
        this.guestTeamId = guestTeamId;
    }
}