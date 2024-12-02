package com.project.superleague.repository;

import com.project.superleague.model.Player;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;


@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class PlayerRepositoryTests {
    @Autowired
    private PlayerRepository playerRepository;

    private Player player1;
    private Player player2;

    @BeforeEach
    public void init() {
        player1 = Player.builder()
                .firstname("Nikos")
                .lastname("Papadimitriou")
                .dateOfBirth(new GregorianCalendar(2000, Calendar.FEBRUARY, 23).getTime())
                .nationality("Greek")
                .monetaryValue(50000)
                .playerRole("Goalkeeper")
                .build();

        player2 = Player.builder()
                .firstname("Giannis")
                .lastname("Papadopoulos")
                .dateOfBirth(new GregorianCalendar(2001, Calendar.AUGUST, 2).getTime())
                .nationality("Greek")
                .monetaryValue(40000)
                .playerRole("Centre-back")
                .build();
    }

    @Test
    public void PlayerRepository_FindByLastnameStartingWith_ReturnsPlayersNotNull() {
        playerRepository.save(player1);
        playerRepository.save(player2);

        List<Player> players  = playerRepository.findByLastnameStartingWith("Papa");

        Assertions.assertThat(players.size()).isEqualTo(2);
    }

    @Test
    public void PlayerRepository_FindByLastnameStartingWith_ReturnsPlayersEmpty() {
        playerRepository.save(player1);
        playerRepository.save(player2);

        List<Player> players  = playerRepository.findByLastnameStartingWith("Xatzi");

        Assertions.assertThat(players).isEmpty();
    }

    @Test
    public void PlayerRepository_FindByLastnameStartingWith_ReturnsAllPlayers() {
        playerRepository.save(player1);
        playerRepository.save(player2);

        List<Player> players  = playerRepository.findByLastnameStartingWith("");

        Assertions.assertThat(players.size()).isEqualTo(playerRepository.count());
    }
}