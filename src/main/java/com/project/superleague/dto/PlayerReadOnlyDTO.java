package com.project.superleague.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@NoArgsConstructor
@Getter
@Setter
public class PlayerReadOnlyDTO extends BaseDTO {
    private String firstname;
    private String lastname;
    private LocalDate dateOfBirth;
    private String nationality;
    private Integer monetaryValue;
    private String playerRole;
    private Long teamId;

    @Builder
    public PlayerReadOnlyDTO(Long id, LocalDate dateOfBirth, String firstname, String lastname, Integer monetaryValue, String nationality, String playerRole, Long teamId) {
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