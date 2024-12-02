package com.project.superleague.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class MatchInsertDTO {
    @NotNull
    private Date matchDate;

    @NotNull
    private Integer goalsHost;

    @NotNull
    private Integer goalsGuest;

    @NotNull
    private Long hostTeamId;

    @NotNull
    private Long guestTeamId;
}