package com.project.superleague.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "MatchesPlayers")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MatchPlayer {
    @EmbeddedId
    private MatchPlayerId id = new MatchPlayerId();

    @ManyToOne
    @MapsId("matchId")
    @JoinColumn(name = "MatchId", referencedColumnName = "id")
    private Match match;

    public void addMatch(Match match) {
        setMatch(match);
        match.getMatchesPlayers().add(this);
    }

    @ManyToOne
    @MapsId("playerId")
    @JoinColumn(name = "PlayerId", referencedColumnName = "id")
    private Player player;

    public void addPlayer(Player player) {
        setPlayer(player);
        player.getMatchesPlayers().add(this);
    }

    @Column(name = "PlayTime")
    private int playTime;

    @Column(name = "Goals")
    private int goals;

    @Column(name = "Assists")
    private int assists;

    @Column(name = "Cards")
    private int cards;
}