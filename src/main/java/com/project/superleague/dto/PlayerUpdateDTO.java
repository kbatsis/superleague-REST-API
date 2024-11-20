package com.project.superleague.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PlayerUpdateDTO extends BaseDTO {
    @NotNull
    @Size(min = 2, max = 20)
    private String firstname;

    @NotNull
    @Size(min = 2, max = 30)
    private String lastname;

    private Date dateOfBirth;

    @Size(min = 2, max = 30)
    private String nationality;

    private Integer monetaryValue;

    @Size(max = 20)
    private String playerRole;

    private Long teamId;

    @Builder
    public PlayerUpdateDTO(Long id, String firstname, String lastname, Date dateOfBirth, String nationality, Integer monetaryValue, String playerRole, Long teamId) {
        setId(id);
        this.firstname = firstname;
        this.lastname = lastname;
        this.dateOfBirth = dateOfBirth;
        this.nationality = nationality;
        this.monetaryValue = monetaryValue;
        this.playerRole = playerRole;
        this.teamId = teamId;
    }
}