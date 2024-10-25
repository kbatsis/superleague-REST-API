package com.project.superleague.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MatchPlayerInsertDTO {
    @NotNull
    private Long matchId;

    @NotNull
    private Long playerId;

    private Integer playTime;

    private Integer goals;

    private Integer assists;

    private Integer cards;
}