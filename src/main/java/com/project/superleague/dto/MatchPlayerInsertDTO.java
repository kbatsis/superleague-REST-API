package com.project.superleague.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
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