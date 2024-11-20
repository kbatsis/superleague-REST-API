package com.project.superleague.repository;

import com.project.superleague.dto.PlayerInsertDTO;
import com.project.superleague.dto.PlayerUpdateDTO;
import com.project.superleague.model.Player;
import com.project.superleague.model.Team;
import com.project.superleague.service.PlayerServiceImpl;
import com.project.superleague.service.exception.EntityNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PlayerServiceTests {
    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private TeamRepository teamRepository;

    @InjectMocks
    private PlayerServiceImpl playerService;

    private Player player;

    private PlayerInsertDTO playerInsertDTO;

    private PlayerUpdateDTO playerUpdateDTO;

    private Team team;

    private Player playerNull;

    private Player updatedPlayer;

    @BeforeEach
    public void init() {
        player = Player.builder()
                .id(1L)
                .firstname("Nikos")
                .lastname("Papadimitriou")
                .dateOfBirth(new GregorianCalendar(2000, Calendar.FEBRUARY, 23).getTime())
                .nationality("Greek")
                .monetaryValue(50000)
                .playerRole("Goalkeeper")
                .build();

        updatedPlayer = Player.builder()
                .firstname("Nikos")
                .lastname("Papadimitris")
                .dateOfBirth(new GregorianCalendar(2000, Calendar.FEBRUARY, 23).getTime())
                .nationality("Greek")
                .monetaryValue(60000)
                .playerRole("Goalkeeper")
                .build();

        playerInsertDTO = PlayerInsertDTO.builder()
                .firstname("Nikos")
                .lastname("Papadimitriou")
                .dateOfBirth(new GregorianCalendar(2000, Calendar.FEBRUARY, 23).getTime())
                .nationality("Greek")
                .monetaryValue(50000)
                .playerRole("Goalkeeper")
                .teamId(1L)
                .build();

        playerUpdateDTO = PlayerUpdateDTO.builder()
                .id(1L)
                .firstname("Nikos")
                .lastname("Papadimitris")
                .dateOfBirth(new GregorianCalendar(2000, Calendar.FEBRUARY, 23).getTime())
                .nationality("Greek")
                .monetaryValue(60000)
                .playerRole("Goalkeeper")
                .teamId(1L)
                .build();

        team = Team.builder()
                .id(1L)
                .teamName("Aris")
                .foundationYear(1914)
                .cityName("Thessaloniki")
                .stadiumName("Kleanthis Vikelidis")
                .coachFirstname("Akis")
                .coachLastname("Mantzios")
                .presidentFirstname("Eirini")
                .presidentLastname("Karypidou")
                .build();

        playerNull = Player.builder().build();
    }

    @Test
    public void PlayerService_InsertPlayer_ReturnsPlayerDTOIdNotNull() throws Exception {
        when(playerRepository.save(Mockito.any(Player.class))).thenReturn(player);
        when(teamRepository.findById(playerInsertDTO.getTeamId())).thenReturn(Optional.ofNullable(team));

        Player savedPlayer = playerService.insertPlayer(playerInsertDTO);

        Assertions.assertThat(savedPlayer.getId()).isNotNull();
    }

    @Test
    public void PlayerService_InsertPlayer_ReturnsException() throws Exception {
        when(playerRepository.save(Mockito.any(Player.class))).thenReturn(playerNull);
        when(teamRepository.findById(playerInsertDTO.getTeamId())).thenReturn(Optional.ofNullable(team));

        Assertions.assertThatThrownBy(() -> playerService.insertPlayer(playerInsertDTO)).isInstanceOf(Exception.class);
    }

    @Test
    public void PlayerService_InsertPlayer_ReturnsEntityNotFoundException() throws EntityNotFoundException {
        when(teamRepository.findById(playerInsertDTO.getTeamId())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> playerService.insertPlayer(playerInsertDTO)).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    public void PlayerService_UpdatePlayer_ReturnsUpdatedPlayerDTO() throws EntityNotFoundException {
        when(playerRepository.findById(playerUpdateDTO.getId())).thenReturn(Optional.ofNullable(player));
        when(teamRepository.findById(playerInsertDTO.getTeamId())).thenReturn(Optional.ofNullable(team));
        when(playerRepository.save(Mockito.any(Player.class))).thenReturn(updatedPlayer);

        Player updateReturn = playerService.updatePlayer(playerUpdateDTO);

        Assertions.assertThat(updateReturn.getLastname()).isEqualTo("Papadimitris");
        Assertions.assertThat(updateReturn.getMonetaryValue()).isEqualTo(60000);
    }

    @Test
    public void PlayerService_UpdatePlayer_ReturnsEntityNotFoundExceptionForPlayer() throws EntityNotFoundException {
        when(playerRepository.findById(playerUpdateDTO.getId())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> playerService.updatePlayer(playerUpdateDTO)).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    public void PlayerService_UpdatePlayer_ReturnsEntityNotFoundExceptionForTeam() throws EntityNotFoundException {
        when(playerRepository.findById(playerUpdateDTO.getId())).thenReturn(Optional.ofNullable(player));
        when(teamRepository.findById(playerInsertDTO.getTeamId())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> playerService.updatePlayer(playerUpdateDTO)).isInstanceOf(EntityNotFoundException.class);
    }
}