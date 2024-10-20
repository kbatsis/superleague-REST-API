package com.project.superleague.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
@Getter
@Setter
public class MatchReadOnlyDTO extends BaseDTO {
    private Date matchDate;

    private int goalsHost;

    private int goalsGuest;

    private Long hostTeamId;

    private Long guestTeamId;

    public MatchReadOnlyDTO(Long id, Date matchDate, int goalsHost, int goalsGuest, Long hostTeamId, Long guestTeamId) {
        setId(id);
        this.matchDate = matchDate;
        this.goalsHost = goalsHost;
        this.goalsGuest = goalsGuest;
        this.hostTeamId = hostTeamId;
        this.guestTeamId = guestTeamId;
    }
}