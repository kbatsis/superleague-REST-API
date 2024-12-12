package com.project.superleague.service;

import com.project.superleague.dto.MatchPlayerInsertDTO;
import com.project.superleague.dto.MatchPlayerUpdateDTO;
import com.project.superleague.model.Match;
import com.project.superleague.model.MatchPlayer;
import com.project.superleague.model.Player;
import com.project.superleague.model.Team;
import com.project.superleague.repository.MatchPlayerRepository;
import com.project.superleague.repository.MatchRepository;
import com.project.superleague.repository.PlayerRepository;
import com.project.superleague.service.exception.EntityAlreadyExistsException;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MatchPlayerServiceTests {
    @Mock
    private MatchPlayerRepository matchPlayerRepository;

    @Mock
    private MatchRepository matchRepository;

    @Mock
    private PlayerRepository playerRepository;

    @InjectMocks
    MatchPlayerServiceImpl matchPlayerService;

    private MatchPlayer matchPlayer;
    private Match match;
    private Player player;
    private Team team1;
    private Team team2;
    private MatchPlayer updatedMatchPlayer;
    private MatchPlayerInsertDTO matchPlayerInsertDTO;
    private MatchPlayerUpdateDTO matchPlayerUpdateDTO;
    private MatchPlayer matchPlayerNull;

    @BeforeEach
    public void init() {
        team1 = Team.builder()
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

        team2 = Team.builder()
                .id(2L)
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
                .id(1L)
                .matchDate(LocalDate.parse("2024-10-04"))
                .goalsHost(1)
                .goalsGuest(2)
                .build();

        match.addHostTeam(team1);
        match.addGuestTeam(team2);

        player = Player.builder()
                .id(1L)
                .firstname("Nikos")
                .lastname("Papadimitriou")
                .dateOfBirth(LocalDate.parse("2000-02-23"))
                .nationality("Greek")
                .monetaryValue(50000)
                .playerRole("Goalkeeper")
                .build();

        player.addTeam(team1);

        matchPlayer = MatchPlayer.builder()
                .id(1L)
                .playTime(15)
                .goals(0)
                .assists(3)
                .cards(1)
                .build();

        matchPlayer.addMatch(match);
        matchPlayer.addPlayer(player);

        updatedMatchPlayer = MatchPlayer.builder()
                .id(1L)
                .playTime(17)
                .goals(0)
                .assists(3)
                .cards(1)
                .build();

        updatedMatchPlayer.addMatch(match);
        updatedMatchPlayer.addPlayer(player);

        matchPlayerInsertDTO = MatchPlayerInsertDTO.builder()
                .playTime(15)
                .goals(0)
                .assists(3)
                .cards(1)
                .matchId(1L)
                .playerId(1L)
                .build();

        matchPlayerUpdateDTO = MatchPlayerUpdateDTO.builder()
                .id(1L)
                .playTime(17)
                .goals(0)
                .assists(3)
                .cards(1)
                .matchId(1L)
                .playerId(1L)
                .build();

        matchPlayerNull = MatchPlayer.builder().build();
    }

    @Test
    public void MatchPlayerService_InsertMatchPlayer_ReturnsMatchPlayerDTOIdNotNull() throws Exception {
        when(matchPlayerRepository.save(Mockito.any(MatchPlayer.class))).thenReturn(matchPlayer);
        when(matchRepository.findById(matchPlayerInsertDTO.getMatchId())).thenReturn(Optional.ofNullable(match));
        when(playerRepository.findById(matchPlayerInsertDTO.getPlayerId())).thenReturn(Optional.ofNullable(player));

        MatchPlayer savedMatchPlayer = matchPlayerService.insertMatchPlayer(matchPlayerInsertDTO);

        Assertions.assertThat(savedMatchPlayer.getId()).isNotNull();
    }

    @Test
    public void MatchPlayerService_InsertMatchPlayer_ThrowsEntityAlreadyExistsException() throws Exception {
        when(matchPlayerRepository.findByMatchIdAndPlayerId(matchPlayerInsertDTO.getMatchId(), matchPlayerInsertDTO.getPlayerId())).thenReturn(Optional.ofNullable(matchPlayer));

        Assertions.assertThatThrownBy(() -> matchPlayerService.insertMatchPlayer(matchPlayerInsertDTO)).isInstanceOf(EntityAlreadyExistsException.class);
    }

    @Test
    public void MatchPlayerService_InsertMatchPlayer_ThrowsException() throws Exception {
        when(matchPlayerRepository.save(Mockito.any(MatchPlayer.class))).thenReturn(matchPlayerNull);
        when(matchRepository.findById(matchPlayerInsertDTO.getMatchId())).thenReturn(Optional.ofNullable(match));
        when(playerRepository.findById(matchPlayerInsertDTO.getPlayerId())).thenReturn(Optional.ofNullable(player));

        Assertions.assertThatThrownBy(() -> matchPlayerService.insertMatchPlayer(matchPlayerInsertDTO)).isInstanceOf(Exception.class);
    }

    @Test
    public void MatchPlayerService_InsertMatchPlayer_ThrowsEntityNotFoundExceptionForMatch() throws EntityNotFoundException {
        when(matchRepository.findById(matchPlayerInsertDTO.getMatchId())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> matchPlayerService.insertMatchPlayer(matchPlayerInsertDTO)).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    public void MatchPlayerService_InsertMatchPlayer_ThrowsEntityNotFoundExceptionForPlayer() throws EntityNotFoundException {
        when(matchRepository.findById(matchPlayerInsertDTO.getMatchId())).thenReturn(Optional.ofNullable(match));
        when(playerRepository.findById(matchPlayerInsertDTO.getPlayerId())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> matchPlayerService.insertMatchPlayer(matchPlayerInsertDTO)).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    public void MatchPlayerService_UpdateMatchPlayer_ReturnsUpdatedMatchPlayerDTO() throws EntityNotFoundException {
        when(matchPlayerRepository.findByMatchIdAndPlayerId(matchPlayerUpdateDTO.getMatchId(), matchPlayerUpdateDTO.getPlayerId())).thenReturn(Optional.ofNullable(matchPlayer));
        when(matchRepository.findById(matchPlayerUpdateDTO.getMatchId())).thenReturn(Optional.ofNullable(match));
        when(playerRepository.findById(matchPlayerUpdateDTO.getPlayerId())).thenReturn(Optional.ofNullable(player));
        when(matchPlayerRepository.save(Mockito.any(MatchPlayer.class))).thenReturn(updatedMatchPlayer);

        MatchPlayer updateReturn = matchPlayerService.updateMatchPlayer(matchPlayerUpdateDTO);

        Assertions.assertThat(updateReturn.getPlayTime()).isEqualTo(17);
    }

    @Test
    public void MatchPlayerService_UpdateMatchPlayer_ThrowsEntityNotFoundExceptionForMatchPlayer() throws EntityNotFoundException {
        when(matchPlayerRepository.findByMatchIdAndPlayerId(matchPlayerUpdateDTO.getMatchId(), matchPlayerUpdateDTO.getPlayerId())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> matchPlayerService.updateMatchPlayer(matchPlayerUpdateDTO)).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    public void MatchPlayerService_UpdateMatchPlayer_ThrowsEntityNotFoundExceptionForMatch() throws EntityNotFoundException {
        when(matchPlayerRepository.findByMatchIdAndPlayerId(matchPlayerUpdateDTO.getMatchId(), matchPlayerUpdateDTO.getPlayerId())).thenReturn(Optional.ofNullable(matchPlayer));
        when(matchRepository.findById(matchPlayerUpdateDTO.getMatchId())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> matchPlayerService.updateMatchPlayer(matchPlayerUpdateDTO)).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    public void MatchPlayerService_UpdateMatchPlayer_ThrowsEntityNotFoundExceptionForPlayer() throws EntityNotFoundException {
        when(matchPlayerRepository.findByMatchIdAndPlayerId(matchPlayerUpdateDTO.getMatchId(), matchPlayerUpdateDTO.getPlayerId())).thenReturn(Optional.ofNullable(matchPlayer));
        when(matchRepository.findById(matchPlayerUpdateDTO.getMatchId())).thenReturn(Optional.ofNullable(match));
        when(playerRepository.findById(matchPlayerUpdateDTO.getPlayerId())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> matchPlayerService.updateMatchPlayer(matchPlayerUpdateDTO)).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    public void MatchPlayerService_DeleteMatchPlayer_ReturnsVoid() {
        Long matchId = 1L;
        Long playerId = 1L;

        when(matchPlayerRepository.findByMatchIdAndPlayerId(matchId, playerId)).thenReturn(Optional.ofNullable(matchPlayer));

        assertAll(() -> matchPlayerService.deleteMatchPlayer(matchId, playerId));
    }

    @Test
    public void MatchPlayerService_DeleteMatchPlayer_ThrowsEntityNotFoundException() {
        Long matchId = 1L;
        Long playerId = 1L;

        when(matchPlayerRepository.findByMatchIdAndPlayerId(matchId, playerId)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> matchPlayerService.deleteMatchPlayer(matchId, playerId)).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    public void MatchPlayerService_GetMatchPlayerByMatchIdAndPlayerId_ReturnsMatchPlayer() throws EntityNotFoundException {
        Long matchId = 1L;
        Long playerId = 1L;

        when(matchPlayerRepository.findByMatchIdAndPlayerId(matchId, playerId)).thenReturn(Optional.ofNullable(matchPlayer));

        MatchPlayer matchPlayerReturn = matchPlayerService.getMatchPlayerByMatchIdAndPlayerId(matchId, playerId);

        Assertions.assertThat(matchPlayerReturn).isNotNull();
    }

    @Test
    public void MatchPlayerService_GetMatchPlayerByMatchIdAndPlayerId_ThrowsEntityNotFoundException() throws EntityNotFoundException {
        Long matchId = 1L;
        Long playerId = 1L;

        when(matchPlayerRepository.findByMatchIdAndPlayerId(matchId, playerId)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> matchPlayerService.getMatchPlayerByMatchIdAndPlayerId(matchId, playerId)).isInstanceOf(EntityNotFoundException.class);
    }
}