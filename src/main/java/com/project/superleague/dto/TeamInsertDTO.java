package com.project.superleague.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class TeamInsertDTO {
    @NotNull
    @Size(min = 2, max = 50)
    private String teamName;

    private Integer foundationYear;

    @NotNull
    @Size(min = 2, max = 30)
    private String cityName;

    @NotNull
    @Size(min = 2, max = 50)
    private String stadiumName;

    @Size(min = 2, max = 20)
    private String coachFirstname;

    @Size(min = 2, max = 30)
    private String coachLastname;

    @Size(min = 2, max = 20)
    private String presidentFirstname;

    @Size(min = 2, max = 30)
    private String presidentLastname;
}
