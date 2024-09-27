package com.project.superleague.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
}
