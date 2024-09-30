package com.project.superleague.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Teams")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Team extends AbstractEntity {
    @Column(name = "Teamname", length = 50, nullable = false)
    private String teamName;

    @Column(name = "FoundationYear")
    private int foundationYear;

    @Column(name = "CityName", length = 30, nullable = false)
    private String cityName;

    @Column(name = "StadiumName", length = 50, nullable = false)
    private String stadiumName;

    @Column(name = "CoachFirstname", length = 20)
    private String coachFirstname;

    @Column(name = "CoachLastname", length = 30)
    private String coachLastname;

    @Column(name = "PresidentFirstname", length = 20)
    private String presidentFirstname;

    @Column(name = "PresidentLastname", length = 30)
    private String presidentLastname;

    @OneToMany(mappedBy = "hostTeam")
    @Getter(AccessLevel.PROTECTED)
    private Set<Match> matchesHost = new HashSet<>();

    public Set<Match> getAllMatchesHost() {
        return Collections.unmodifiableSet(matchesHost);
    }

    @OneToMany(mappedBy = "guestTeam")
    @Getter(AccessLevel.PROTECTED)
    private Set<Match> matchesGuest= new HashSet<>();

    public Set<Match> getAllMatchesGuest() {
        return Collections.unmodifiableSet(matchesGuest);
    }

    @OneToMany(mappedBy = "team")
    @Getter(AccessLevel.PROTECTED)
    private Set<Player> players = new HashSet<>();

    public Set<Player> getAllPlayers() {
        return Collections.unmodifiableSet(players);
    }

    public void addPlayer(Player player) {
        players.add(player);
        player.setTeam(this);
    }

    public void deletePlayer(Player player) {
        players.remove(player);
        player.setTeam(null);
    }
}