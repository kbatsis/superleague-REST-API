package com.project.superleague.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.security.SecureRandom;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class PlayerInsertDTO {
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
}