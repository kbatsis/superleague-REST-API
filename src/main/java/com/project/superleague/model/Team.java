package com.project.superleague.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Teams")
@NoArgsConstructor
@Getter
@Setter
public class Team extends AbstractEntity {
    @Column(name = "Teamname", length = 50, nullable = false)
    private String teamName;

    @Column(name = "FoundationYear")
    private Integer foundationYear;

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

    @OneToMany(mappedBy = "hostTeam", cascade = CascadeType.REMOVE)
    @Getter(AccessLevel.PROTECTED)
    private Set<Match> matchesHost = new HashSet<>();

    public Set<Match> getAllMatchesHost() {
        return Collections.unmodifiableSet(matchesHost);
    }

    @OneToMany(mappedBy = "guestTeam", cascade = CascadeType.REMOVE)
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

    @Builder
    public Team(Long id, String teamName, Integer foundationYear, String cityName, String stadiumName, String coachFirstname, String coachLastname, String presidentFirstname, String presidentLastname) {
        setId(id);
        this.teamName = teamName;
        this.foundationYear = foundationYear;
        this.cityName = cityName;
        this.stadiumName = stadiumName;
        this.coachFirstname = coachFirstname;
        this.coachLastname = coachLastname;
        this.presidentFirstname = presidentFirstname;
        this.presidentLastname = presidentLastname;
    }
}