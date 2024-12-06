package com.project.superleague.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MatchUpdateDTO extends BaseDTO {
    @NotNull
    private LocalDate matchDate;

    @NotNull
    private Integer goalsHost;

    @NotNull
    private Integer goalsGuest;

    @NotNull
    private Long hostTeamId;

    @NotNull
    private Long guestTeamId;

    @Builder
    public MatchUpdateDTO(Long id, LocalDate matchDate, Integer goalsHost, Integer goalsGuest, Long hostTeamId, Long guestTeamId) {
        setId(id);
        this.matchDate = matchDate;
        this.goalsHost = goalsHost;
        this.goalsGuest = goalsGuest;
        this.hostTeamId = hostTeamId;
        this.guestTeamId = guestTeamId;
    }
}