package com.project.superleague.repository;

import com.project.superleague.model.Player;
import com.project.superleague.model.Team;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class TeamRepositoryTests {
    @Autowired
    private TeamRepository teamRepository;

    private Team team1;
    private Team team2;

    @BeforeEach
    public void init() {
        team1 = Team.builder()
                .teamName("Aris")
                .foundationYear(1914)
                .cityName("Thessaloniki")
                .stadiumName("Kleanthis Vikelidis")
                .coachFirstname("Akis")
                .coachLastname("Mantzios")
                .presidentFirstname("Eirini")
                .presidentLastname("Karypidou")
                .build();

        team2 = Team.builder()
                .teamName("Ofi")
                .foundationYear(1925)
                .cityName("Irakleio")
                .stadiumName("Theodoros Bardinogiannis")
                .coachFirstname("Milan")
                .coachLastname("Rastavats")
                .presidentFirstname("Mihail")
                .presidentLastname("Mpousis")
                .build();
    }

    @Test
    public void TeamRepository_FindByTeamNameStartingWith_ReturnsTeamsNotNull() {
        teamRepository.save(team1);
        teamRepository.save(team2);

        List<Team> teams  = teamRepository.findByTeamNameStartingWith("Ar");

        Assertions.assertThat(teams.size()).isEqualTo(1);
    }

    @Test
    public void TeamRepository_FindByTeamNameStartingWith_ReturnsTeamsEmpty() {
        teamRepository.save(team1);
        teamRepository.save(team2);

        List<Team> teams  = teamRepository.findByTeamNameStartingWith("Leva");

        Assertions.assertThat(teams).isEmpty();
    }

    @Test
    public void TeamRepository_FindByTeamNameStartingWith_ReturnsAllTeams() {
        teamRepository.save(team1);
        teamRepository.save(team2);

        List<Team> teams  = teamRepository.findByTeamNameStartingWith("");

        Assertions.assertThat(teams.size()).isEqualTo(teamRepository.count());
    }
}