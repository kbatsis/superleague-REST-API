package com.project.superleague.model;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class MatchPlayerId implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long matchId;
    private Long playerId;
}
