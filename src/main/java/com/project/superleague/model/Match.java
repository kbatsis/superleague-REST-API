package com.project.superleague.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Matches")
@NoArgsConstructor
@Getter
@Setter
public class Match extends AbstractEntity {
    @Column(name = "MatchDate", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date matchDate;

    @Column(name = "GoalsHost", nullable = false)
    private Integer goalsHost;

    @Column(name = "GoalsGuest", nullable = false)
    private Integer goalsGuest;

    @OneToMany(mappedBy = "match", cascade = CascadeType.REMOVE)
    @Getter(AccessLevel.PROTECTED)
    private Set<MatchPlayer> matchesPlayers = new HashSet<>();

    public Set<MatchPlayer> getAllMatchesPlayers() {
        return Collections.unmodifiableSet(matchesPlayers);
    }

    @ManyToOne
    @JoinColumn(name = "HostTeam", referencedColumnName = "id", nullable = false)
    private Team hostTeam;

    public void addHostTeam(Team team) {
        setHostTeam(team);
        team.getMatchesHost().add(this);
    }

    public void deleteHostTeam(Team team) {
        setHostTeam(null);
        team.getMatchesHost().remove(this);
    }

    @ManyToOne
    @JoinColumn(name = "GuestTeam", referencedColumnName = "id", nullable = false)
    private Team guestTeam;

    public void addGuestTeam(Team team) {
        setGuestTeam(team);
        team.getMatchesGuest().add(this);
    }

    public void deleteGuestTeam(Team team) {
        setGuestTeam(null);
        team.getMatchesGuest().remove(this);
    }

    public Match(Long id, Date matchDate, Integer goalsHost, Integer goalsGuest) {
        setId(id);
        this.matchDate = matchDate;
        this.goalsHost = goalsHost;
        this.goalsGuest = goalsGuest;
    }
}