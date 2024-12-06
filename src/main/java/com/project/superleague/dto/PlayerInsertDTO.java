package com.project.superleague.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

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

    private LocalDate dateOfBirth;

    @Size(min = 2, max = 30)
    private String nationality;

    private Integer monetaryValue;

    @Size(max = 20)
    private String playerRole;

    @NotNull
    private Long teamId;
}