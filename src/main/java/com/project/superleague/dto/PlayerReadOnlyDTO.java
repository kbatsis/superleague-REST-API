package com.project.superleague.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
@Getter
@Setter
public class PlayerReadOnlyDTO extends BaseDTO {
    private String firstname;
    private String lastname;
    private Date dateOfBirth;
    private String nationality;
    private Integer monetaryValue;
    private String playerRole;
    private Long teamId;

    public PlayerReadOnlyDTO(Long id, Date dateOfBirth, String firstname, String lastname, Integer monetaryValue, String nationality, String playerRole, Long teamId) {
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