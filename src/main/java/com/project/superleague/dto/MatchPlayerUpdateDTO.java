package com.project.superleague.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MatchPlayerUpdateDTO extends BaseDTO {
    @NotNull
    private Long matchId;

    @NotNull
    private Long playerId;

    private Integer playTime;

    private Integer goals;

    private Integer assists;

    private Integer cards;

    @Builder
    public MatchPlayerUpdateDTO(Long id, Long matchId, Long playerId, Integer playTime, Integer goals, Integer assists, Integer cards) {
        setId(id);
        this.matchId = matchId;
        this.playerId = playerId;
        this.playTime = playTime;
        this.goals = goals;
        this.assists = assists;
        this.cards = cards;
    }
}