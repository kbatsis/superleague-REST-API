package com.project.superleague.repository;

import com.project.superleague.model.Match;
import com.project.superleague.model.MatchPlayer;
import com.project.superleague.model.Player;
import com.project.superleague.model.Team;
import com.project.superleague.service.exception.EntityNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.Optional;


@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class MatchPlayerRepositoryTests {
    @Autowired
    private MatchPlayerRepository matchPlayerRepository;

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private TeamRepository teamRepository;

    private MatchPlayer matchPlayer;
    private Match match;
    private Player player;
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

        match = Match.builder()
                .matchDate(LocalDate.parse("2024-10-04"))
                .goalsHost(1)
                .goalsGuest(2)
                .build();

        match.addHostTeam(team1);
        match.addGuestTeam(team2);

        player = Player.builder()
                .firstname("Nikos")
                .lastname("Papadimitriou")
                .dateOfBirth(LocalDate.parse("2000-02-23"))
                .nationality("Greek")
                .monetaryValue(50000)
                .playerRole("Goalkeeper")
                .build();

        player.addTeam(team1);

        matchPlayer = MatchPlayer.builder()
                .playTime(15)
                .goals(0)
                .assists(3)
                .cards(1)
                .build();

        matchPlayer.addMatch(match);
        matchPlayer.addPlayer(player);
    }

    @Test
    public void MatchPlayerRepository_FindByMatchIdAndPlayerId_ReturnsOptionalMatchPlayer() {
        teamRepository.save(team1);
        teamRepository.save(team2);
        matchRepository.save(match);
        playerRepository.save(player);
        matchPlayerRepository.save(matchPlayer);

        Optional<MatchPlayer> matchPlayerReturn =  matchPlayerRepository.findByMatchIdAndPlayerId(match.getId(), player.getId());

        Assertions.assertThat(matchPlayerReturn).isPresent();
    }

    @Test
    public void MatchPlayerRepository_FindByMatchIdAndPlayerId_ReturnsOptionalNull() {
        teamRepository.save(team1);
        teamRepository.save(team2);
        matchRepository.save(match);
        playerRepository.save(player);
        matchPlayerRepository.save(matchPlayer);

        Optional<MatchPlayer> matchPlayerReturn =  matchPlayerRepository.findByMatchIdAndPlayerId(20L ,20L);

        Assertions.assertThat(matchPlayerReturn).isNotPresent();
    }

    @Test
    public void MatchPlayerRepository_DeleteByMatchIdAndPlayerId_ReturnsDeletedRecordsCount() {
        teamRepository.save(team1);
        teamRepository.save(team2);
        matchRepository.save(match);
        playerRepository.save(player);
        matchPlayerRepository.save(matchPlayer);

        Long deletedRecordCount = matchPlayerRepository.deleteByMatchIdAndPlayerId(match.getId(),player.getId());

        Assertions.assertThat(deletedRecordCount).isEqualTo(1L);
    }

    @Test
    public void MatchPlayerRepository_DeleteByMatchIdAndPlayerId_ReturnsZeroDeletedRecordsCount() {
        teamRepository.save(team1);
        teamRepository.save(team2);
        matchRepository.save(match);
        playerRepository.save(player);
        matchPlayerRepository.save(matchPlayer);

        Long deletedRecordCount = matchPlayerRepository.deleteByMatchIdAndPlayerId(20L, 20L);

        Assertions.assertThat(deletedRecordCount).isEqualTo(0L);
    }
}