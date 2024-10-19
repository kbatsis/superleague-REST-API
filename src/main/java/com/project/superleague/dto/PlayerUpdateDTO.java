package com.project.superleague.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    private int monetaryValue;

    @Size(max = 20)
    private String playerRole;

    private Long teamId;
}