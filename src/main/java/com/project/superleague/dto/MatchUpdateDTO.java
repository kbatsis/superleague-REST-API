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
public class MatchUpdateDTO extends BaseDTO {
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