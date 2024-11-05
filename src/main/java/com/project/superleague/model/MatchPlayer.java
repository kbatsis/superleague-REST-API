package com.project.superleague.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "MatchesPlayers", uniqueConstraints = { @UniqueConstraint(name = "UniqueMatchAndPlayerId", columnNames = {"MatchId", "PlayerId"})})
@NoArgsConstructor
@Getter
@Setter
public class MatchPlayer extends AbstractEntity {
    @ManyToOne
    @JoinColumn(name = "MatchId", referencedColumnName = "id", nullable = false)
    private Match match;

    public void addMatch(Match match) {
        setMatch(match);
        match.getMatchesPlayers().add(this);
    }

    @ManyToOne
    @JoinColumn(name = "PlayerId", referencedColumnName = "id", nullable = false)
    private Player player;

    public void addPlayer(Player player) {
        setPlayer(player);
        player.getMatchesPlayers().add(this);
    }

    @Column(name = "PlayTime")
    private Integer playTime;

    @Column(name = "Goals")
    private Integer goals;

    @Column(name = "Assists")
    private Integer assists;

    @Column(name = "Cards")
    private Integer cards;

    public MatchPlayer(Long id, Integer playTime, Integer goals, Integer assists, Integer cards) {
        setId(id);
        this.playTime = playTime;
        this.goals = goals;
        this.assists = assists;
        this.cards = cards;
    }
}