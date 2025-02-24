package com.project.superleague.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class MatchPlayerReadOnlyDTO extends BaseDTO {
    private Long matchId;
    private Long playerId;
    private Integer playTime;
    private Integer goals;
    private Integer assists;
    private Integer cards;

    public MatchPlayerReadOnlyDTO(Long id, Long matchId, Long playerId, Integer playTime, Integer goals, Integer assists, Integer cards) {
        setId(id);
        this.matchId = matchId;
        this.playerId = playerId;
        this.playTime = playTime;
        this.goals = goals;
        this.assists = assists;
        this.cards = cards;
    }
}