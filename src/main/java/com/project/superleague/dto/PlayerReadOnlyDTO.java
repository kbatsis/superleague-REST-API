package com.project.superleague.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
@Getter
@Setter
public class PlayerReadOnlyDTO extends BaseDTO {
    private Date dateOfBirth;
    private String firstname;
    private String lastname;
    private int monetaryValue;
    private String nationality;
    private String playerRole;
    private Long teamId;

    public PlayerReadOnlyDTO(Long id, Date dateOfBirth, String firstname, String lastname, int monetaryValue, String nationality, String playerRole, Long teamId) {
        setId(id);
        this.dateOfBirth = dateOfBirth;
        this.firstname = firstname;
        this.lastname = lastname;
        this.monetaryValue = monetaryValue;
        this.nationality = nationality;
        this.playerRole = playerRole;
        this.teamId = teamId;
    }
}