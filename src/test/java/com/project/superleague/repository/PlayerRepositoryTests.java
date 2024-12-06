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

import java.time.LocalDate;
import java.util.List;


@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class PlayerRepositoryTests {
    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private TeamRepository teamRepository;

    private Player player1;
    private Player player2;
    private Team team;

    @BeforeEach
    public void init() {
        team = Team.builder()
                .teamName("Aris")
                .foundationYear(1914)
                .cityName("Thessaloniki")
                .stadiumName("Kleanthis Vikelidis")
                .coachFirstname("Akis")
                .coachLastname("Mantzios")
                .presidentFirstname("Eirini")
                .presidentLastname("Karypidou")
                .build();

        player1 = Player.builder()
                .firstname("Nikos")
                .lastname("Papadimitriou")
                .dateOfBirth(LocalDate.parse("2000-02-23"))
                .nationality("Greek")
                .monetaryValue(50000)
                .playerRole("Goalkeeper")
                .build();

        player1.addTeam(team);

        player2 = Player.builder()
                .firstname("Giannis")
                .lastname("Papadopoulos")
                .dateOfBirth(LocalDate.parse("2001-08-02"))
                .nationality("Greek")
                .monetaryValue(40000)
                .playerRole("Centre-back")
                .build();

        player2.addTeam(team);
    }

    @Test
    public void PlayerRepository_FindByLastnameStartingWith_ReturnsPlayersNotNull() {
        teamRepository.save(team);
        playerRepository.save(player1);
        playerRepository.save(player2);

        List<Player> players  = playerRepository.findByLastnameStartingWith("Papa");

        Assertions.assertThat(players.size()).isEqualTo(2);
    }

    @Test
    public void PlayerRepository_FindByLastnameStartingWith_ReturnsPlayersEmpty() {
        teamRepository.save(team);
        playerRepository.save(player1);
        playerRepository.save(player2);

        List<Player> players  = playerRepository.findByLastnameStartingWith("Xatzi");

        Assertions.assertThat(players).isEmpty();
    }

    @Test
    public void PlayerRepository_FindByLastnameStartingWith_ReturnsAllPlayers() {
        teamRepository.save(team);
        playerRepository.save(player1);
        playerRepository.save(player2);

        List<Player> players  = playerRepository.findByLastnameStartingWith("");

        Assertions.assertThat(players.size()).isEqualTo(playerRepository.count());
    }
}