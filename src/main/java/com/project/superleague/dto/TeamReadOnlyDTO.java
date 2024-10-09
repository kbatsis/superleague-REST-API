package com.project.superleague.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class TeamReadOnlyDTO extends BaseDTO {
    private String teamName;
    private int foundationYear;
    private String cityName;
    private String stadiumName;
    private String coachFirstname;
    private String coachLastname;
    private String presidentFirstname;
    private String presidentLastname;

    public TeamReadOnlyDTO(Long id, String teamName, int foundationYear, String cityName, String stadiumName, String coachFirstname, String coachLastname, String presidentFirstname, String presidentLastname) {
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
