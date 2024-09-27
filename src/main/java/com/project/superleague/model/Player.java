package com.project.superleague.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "Players")
@NoArgsConstructor
@AllArgsConstructor
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
    private int monetaryValue;

    @Column(name = "PlayerRole", length = 20)
    private String playerRole;
}
