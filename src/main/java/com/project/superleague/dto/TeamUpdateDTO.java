package com.project.superleague.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TeamUpdateDTO extends BaseDTO {
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

    @Builder
    public TeamUpdateDTO(Long id, String teamName, Integer foundationYear, String cityName, String stadiumName, String coachFirstname, String coachLastname, String presidentFirstname, String presidentLastname) {
        setId(id);
        this.teamName = teamName;
        this.foundationYear = foundationYear;
        this.cityName = cityName;
        this.stadiumName = stadiumName;
        this.coachFirstname = coachFirstname;
        this.coachLastname = coachLastname;
        this.presidentFirstname = presidentFirstname;
        this.presidentLastname = presidentLastname;
    }
}
