package com.project.superleague.model;


import jakarta.persistence.*;
import lombok.*;

import java.util.Collections;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "Players")
@NoArgsConstructor
@Getter
@Setter
public class Player extends AbstractEntity {
    @Column(name = "Firstname", length = 20, nullable = false)
    private String firstname;

    @Column(name = "Lastname", length = 30, nullable = false)
    private String lastname;

    @Column(name = "DateOfBirth")
    @Temporal(TemporalType.DATE)
    private Date dateOfBirth;

    @Column(name = "Nationality", length = 30)
    private String nationality;

    @Column(name = "MonetaryValue")
    private Integer monetaryValue;

    @Column(name = "PlayerRole", length = 20)
    private String playerRole;

    @ManyToOne
    @JoinColumn(name = "TeamId", referencedColumnName = "id")
    private Team team;

    public void addTeam(Team team) {
        setTeam(team);
        if (team != null) {
            team.getPlayers().add(this);
        }
    }

    public void deleteTeam(Team team) {
        setTeam(null);
        team.getPlayers().remove(this);
    }

    @OneToMany(mappedBy = "player", cascade = CascadeType.REMOVE)
    @Getter(AccessLevel.PROTECTED)
    private Set<MatchPlayer> matchesPlayers;

    public Set<MatchPlayer> getAllMatchesPlayers() {
        return Collections.unmodifiableSet(matchesPlayers);
    }

    public Player(Long id, String firstname, String lastname, Date dateOfBirth, String nationality, Integer monetaryValue, String playerRole) {
        setId(id);
        this.firstname = firstname;
        this.lastname = lastname;
        this.dateOfBirth = dateOfBirth;
        this.nationality = nationality;
        this.monetaryValue = monetaryValue;
        this.playerRole = playerRole;
    }
}