package com.project.superleague.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MatchInsertDTO {
    @NotNull
    private Date matchDate;

    private int goalsHost;

    private int goalsGuest;

    private Long hostTeamId;

    private Long guestTeamId;
}