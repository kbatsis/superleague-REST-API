package com.project.superleague.repository;

import com.project.superleague.model.Match;
import com.project.superleague.model.Player;
import com.project.superleague.model.Team;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class MatchRepositoryTests {
    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private TeamRepository teamRepository;

    private Match match1;
    private Match match2;
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

        match1 = Match.builder()
                .matchDate(LocalDate.parse("2024-10-04"))
                .goalsHost(1)
                .goalsGuest(2)
                .build();

        match1.addHostTeam(team1);
        match1.addGuestTeam(team2);

        match2 = Match.builder()
                .matchDate(LocalDate.parse("2024-10-27"))
                .goalsHost(0)
                .goalsGuest(1)
                .build();

        match2.addHostTeam(team2);
        match2.addGuestTeam(team1);
    }

    @Test
    public void MatchRepository_FindByMatchDate_ReturnsMatchesNotNull() {
        teamRepository.save(team1);
        teamRepository.save(team2);
        matchRepository.save(match1);
        matchRepository.save(match2);

        List<Match> matches  = matchRepository.findByMatchDate(LocalDate.parse("2024-10-27"));

        Assertions.assertThat(matches.size()).isEqualTo(1);
    }

    @Test
    public void MatchRepository_FindByMatchDate_ReturnsMatchesEmpty() {
        teamRepository.save(team1);
        teamRepository.save(team2);
        matchRepository.save(match1);
        matchRepository.save(match2);

        List<Match> matches  = matchRepository.findByMatchDate(LocalDate.parse("2024-10-15"));

        Assertions.assertThat(matches).isEmpty();
    }
}