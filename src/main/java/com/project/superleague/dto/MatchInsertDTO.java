package com.project.superleague.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class MatchInsertDTO {
    @NotNull
    private LocalDate matchDate;

    @NotNull
    private Integer goalsHost;

    @NotNull
    private Integer goalsGuest;

    @NotNull
    private Long hostTeamId;

    @NotNull
    private Long guestTeamId;
}