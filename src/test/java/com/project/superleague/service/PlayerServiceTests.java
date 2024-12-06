package com.project.superleague.service;

import com.project.superleague.dto.PlayerInsertDTO;
import com.project.superleague.dto.PlayerUpdateDTO;
import com.project.superleague.model.Player;
import com.project.superleague.model.Team;
import com.project.superleague.repository.PlayerRepository;
import com.project.superleague.repository.TeamRepository;
import com.project.superleague.service.exception.EntityNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
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

        player = Player.builder()
                .id(1L)
                .firstname("Nikos")
                .lastname("Papadimitriou")
                .dateOfBirth(LocalDate.parse("2000-02-23"))
                .nationality("Greek")
                .monetaryValue(50000)
                .playerRole("Goalkeeper")
                .build();

        player.addTeam(team);

        updatedPlayer = Player.builder()
                .id(1L)
                .firstname("Nikos")
                .lastname("Papadimitris")
                .dateOfBirth(LocalDate.parse("2000-02-23"))
                .nationality("Greek")
                .monetaryValue(60000)
                .playerRole("Goalkeeper")
                .build();

        updatedPlayer.addTeam(team);

        playerInsertDTO = PlayerInsertDTO.builder()
                .firstname("Nikos")
                .lastname("Papadimitriou")
                .dateOfBirth(LocalDate.parse("2000-02-23"))
                .nationality("Greek")
                .monetaryValue(50000)
                .playerRole("Goalkeeper")
                .teamId(1L)
                .build();

        playerUpdateDTO = PlayerUpdateDTO.builder()
                .id(1L)
                .firstname("Nikos")
                .lastname("Papadimitris")
                .dateOfBirth(LocalDate.parse("2000-02-23"))
                .nationality("Greek")
                .monetaryValue(60000)
                .playerRole("Goalkeeper")
                .teamId(1L)
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
    public void PlayerService_InsertPlayer_ThrowsException() throws Exception {
        when(playerRepository.save(Mockito.any(Player.class))).thenReturn(playerNull);
        when(teamRepository.findById(playerInsertDTO.getTeamId())).thenReturn(Optional.ofNullable(team));

        Assertions.assertThatThrownBy(() -> playerService.insertPlayer(playerInsertDTO)).isInstanceOf(Exception.class);
    }

    @Test
    public void PlayerService_InsertPlayer_ThrowsEntityNotFoundException() throws EntityNotFoundException {
        when(teamRepository.findById(playerInsertDTO.getTeamId())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> playerService.insertPlayer(playerInsertDTO)).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    public void PlayerService_UpdatePlayer_ReturnsUpdatedPlayerDTO() throws EntityNotFoundException {
        when(playerRepository.findById(playerUpdateDTO.getId())).thenReturn(Optional.ofNullable(player));
        when(teamRepository.findById(playerUpdateDTO.getTeamId())).thenReturn(Optional.ofNullable(team));
        when(playerRepository.save(Mockito.any(Player.class))).thenReturn(updatedPlayer);

        Player updateReturn = playerService.updatePlayer(playerUpdateDTO);

        Assertions.assertThat(updateReturn.getLastname()).isEqualTo("Papadimitris");
        Assertions.assertThat(updateReturn.getMonetaryValue()).isEqualTo(60000);
    }

    @Test
    public void PlayerService_UpdatePlayer_ThrowsEntityNotFoundExceptionForPlayer() throws EntityNotFoundException {
        when(playerRepository.findById(playerUpdateDTO.getId())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> playerService.updatePlayer(playerUpdateDTO)).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    public void PlayerService_UpdatePlayer_ThrowsEntityNotFoundExceptionForTeam() throws EntityNotFoundException {
        when(playerRepository.findById(playerUpdateDTO.getId())).thenReturn(Optional.ofNullable(player));
        when(teamRepository.findById(playerUpdateDTO.getTeamId())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> playerService.updatePlayer(playerUpdateDTO)).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    public void PlayerService_DeletePlayer_ReturnsVoid() {
        Long playerId = 1L;

        when(playerRepository.findById(playerId)).thenReturn(Optional.ofNullable(player));

        assertAll(() -> playerService.deletePlayer(playerId));
    }

    @Test
    public void PlayerService_DeletePlayer_ThrowsEntityNotFoundException() {
        Long playerId = 1L;

        when(playerRepository.findById(playerId)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> playerService.deletePlayer(playerId)).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    public void PlayerService_GetPlayerByLastName_ReturnsPlayers() throws EntityNotFoundException {
        String searchParameter = "Papa";
        List<Player> players = new ArrayList<>();
        players.add(player);

        when(playerRepository.findByLastnameStartingWith(searchParameter)).thenReturn(players);

        List<Player> playersReturn = playerService.getPlayerByLastname(searchParameter);

        Assertions.assertThat(playersReturn.size()).isEqualTo(1);
    }

    @Test
    public void PlayerService_GetPlayerByLastName_ThrowsEntityNotFoundException() throws EntityNotFoundException {
        String searchParameter = "Papa";
        List<Player> players = new ArrayList<>();

        when(playerRepository.findByLastnameStartingWith(searchParameter)).thenReturn(players);

        Assertions.assertThatThrownBy(() -> playerService.getPlayerByLastname(searchParameter)).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    public void PlayerService_GetPlayerById_ReturnsPlayer() throws EntityNotFoundException {
        Long playerId = 1L;

        when(playerRepository.findById(playerId)).thenReturn(Optional.ofNullable(player));

        Player playerReturn = playerService.getPlayerById(playerId);

        Assertions.assertThat(playerReturn).isNotNull();
    }

    @Test
    public void PlayerService_GetPlayerById_ThrowsEntityNotFoundException() throws EntityNotFoundException {
        Long playerId = 1L;

        when(playerRepository.findById(playerId)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> playerService.getPlayerById(playerId)).isInstanceOf(EntityNotFoundException.class);
    }
}