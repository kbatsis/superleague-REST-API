package com.project.superleague.service;

import com.project.superleague.dto.MatchInsertDTO;
import com.project.superleague.dto.MatchUpdateDTO;
import com.project.superleague.model.Match;
import com.project.superleague.model.Player;
import com.project.superleague.model.Team;
import com.project.superleague.repository.MatchRepository;
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

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MatchServiceTests {
    @Mock
    private MatchRepository matchRepository;

    @Mock
    private TeamRepository teamRepository;

    @InjectMocks
    private MatchServiceImpl matchService;

    private Match match;
    private Team team1;
    private Team team2;
    private MatchInsertDTO matchInsertDTO;
    private MatchUpdateDTO matchUpdateDTO;
    private Match matchNull;
    private Match updatedMatch;

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
                .matchDate(new GregorianCalendar(2024, Calendar.OCTOBER, 4).getTime())
                .goalsHost(1)
                .goalsGuest(2)
                .build();

        match.addHostTeam(team1);
        match.addGuestTeam(team2);

        matchInsertDTO = MatchInsertDTO.builder()
                .matchDate(new GregorianCalendar(2024, Calendar.OCTOBER, 4).getTime())
                .goalsHost(1)
                .goalsGuest(2)
                .hostTeamId(1L)
                .guestTeamId(2L)
                .build();

        matchUpdateDTO = MatchUpdateDTO.builder()
                .id(1L)
                .matchDate(new GregorianCalendar(2024, Calendar.NOVEMBER, 4).getTime())
                .goalsHost(1)
                .goalsGuest(0)
                .hostTeamId(1L)
                .guestTeamId(2L)
                .build();

        matchNull = Match.builder().build();

        updatedMatch = Match.builder()
                .id(1L)
                .matchDate(new GregorianCalendar(2024, Calendar.NOVEMBER, 4).getTime())
                .goalsHost(1)
                .goalsGuest(0)
                .build();

        updatedMatch.addHostTeam(team1);
        updatedMatch.addGuestTeam(team2);
    }

    @Test
    public void MatchService_InsertMatch_ReturnsMatchDTOIdNotNull() throws Exception {
        when(matchRepository.save(Mockito.any(Match.class))).thenReturn(match);
        when(teamRepository.findById(matchInsertDTO.getHostTeamId())).thenReturn(Optional.ofNullable(team1));
        when(teamRepository.findById(matchInsertDTO.getGuestTeamId())).thenReturn(Optional.ofNullable(team2));

        Match savedMatch = matchService.insertMatch(matchInsertDTO);

        Assertions.assertThat(savedMatch.getId()).isNotNull();
    }

    @Test
    public void MatchService_InsertMatch_ThrowsException() throws Exception {
        when(matchRepository.save(Mockito.any(Match.class))).thenReturn(matchNull);
        when(teamRepository.findById(matchInsertDTO.getHostTeamId())).thenReturn(Optional.ofNullable(team1));
        when(teamRepository.findById(matchInsertDTO.getGuestTeamId())).thenReturn(Optional.ofNullable(team2));

        Assertions.assertThatThrownBy(() -> matchService.insertMatch(matchInsertDTO)).isInstanceOf(Exception.class);
    }

    @Test
    public void MatchService_InsertMatch_HostTeamNotFound_ThrowsEntityNotFoundException() throws EntityNotFoundException {
        when(teamRepository.findById(matchInsertDTO.getHostTeamId())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> matchService.insertMatch(matchInsertDTO)).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    public void MatchService_InsertMatch_GuestTeamNotFound_ThrowsEntityNotFoundException() throws EntityNotFoundException {
        when(teamRepository.findById(matchInsertDTO.getHostTeamId())).thenReturn(Optional.ofNullable(team1));
        when(teamRepository.findById(matchInsertDTO.getGuestTeamId())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> matchService.insertMatch(matchInsertDTO)).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    public void MatchService_UpdateMatch_ReturnsUpdatedMatchDTO() throws EntityNotFoundException {
        when(matchRepository.findById(matchUpdateDTO.getId())).thenReturn(Optional.ofNullable(match));
        when(teamRepository.findById(matchUpdateDTO.getHostTeamId())).thenReturn(Optional.ofNullable(team1));
        when(teamRepository.findById(matchUpdateDTO.getGuestTeamId())).thenReturn(Optional.ofNullable(team2));
        when(matchRepository.save(Mockito.any(Match.class))).thenReturn(updatedMatch);

        Match updateReturn = matchService.updateMatch(matchUpdateDTO);

        Assertions.assertThat(updateReturn.getMatchDate()).isEqualTo(new GregorianCalendar(2024, Calendar.NOVEMBER, 4).getTime());
        Assertions.assertThat(updateReturn.getGoalsGuest()).isEqualTo(0);
    }

    @Test
    public void MatchService_UpdateMatch_MatchNotFound_ThrowsEntityNotFoundException() throws EntityNotFoundException {
        when(matchRepository.findById(matchUpdateDTO.getId())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> matchService.updateMatch(matchUpdateDTO)).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    public void MatchService_UpdateMatch_HostTeamNotFound_ThrowsEntityNotFoundException() throws EntityNotFoundException {
        when(matchRepository.findById(matchUpdateDTO.getId())).thenReturn(Optional.ofNullable(match));
        when(teamRepository.findById(matchUpdateDTO.getHostTeamId())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> matchService.updateMatch(matchUpdateDTO)).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    public void MatchService_UpdateMatch_GuestTeamNotFound_ThrowsEntityNotFoundException() throws EntityNotFoundException {
        when(matchRepository.findById(matchUpdateDTO.getId())).thenReturn(Optional.ofNullable(match));
        when(teamRepository.findById(matchUpdateDTO.getHostTeamId())).thenReturn(Optional.ofNullable(team1));
        when(teamRepository.findById(matchUpdateDTO.getGuestTeamId())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> matchService.updateMatch(matchUpdateDTO)).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    public void MatchService_DeleteMatch_ReturnsVoid() {
        Long matchId = 1L;

        when(matchRepository.findById(matchId)).thenReturn(Optional.ofNullable(match));

        assertAll(() -> matchService.deleteMatch(matchId));
    }

    @Test
    public void MatchService_DeleteMatch_ThrowsEntityNotFoundException() {
        Long matchId = 1L;

        when(matchRepository.findById(matchId)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> matchService.deleteMatch(matchId)).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    public void MatchService_GetMatchByDate_ReturnsMatches() throws EntityNotFoundException {
        Date searchParameter = new GregorianCalendar(2024, Calendar.OCTOBER, 4).getTime();
        List<Match> matches = new ArrayList<>();
        matches.add(match);

        when(matchRepository.findByMatchDate(searchParameter)).thenReturn(matches);

        List<Match> matchesReturn = matchService.getMatchByDate(searchParameter);

        Assertions.assertThat(matchesReturn.size()).isEqualTo(1);
    }

    @Test
    public void MatchService_GetMatchByDate_ThrowsEntityNotFoundException() throws EntityNotFoundException {
        Date searchParameter = new GregorianCalendar(2024, Calendar.OCTOBER, 4).getTime();
        List<Match> matches = new ArrayList<>();

        when(matchRepository.findByMatchDate(searchParameter)).thenReturn(matches);

        Assertions.assertThatThrownBy(() -> matchService.getMatchByDate(searchParameter)).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    public void MatchService_GetMatchById_ReturnsMatch() throws EntityNotFoundException {
        Long matchId = 1L;

        when(matchRepository.findById(matchId)).thenReturn(Optional.ofNullable(match));

        Match matchReturn = matchService.getMatchById(matchId);

        Assertions.assertThat(matchReturn).isNotNull();
    }

    @Test
    public void MatchService_GetMatchById_ThrowsEntityNotFoundException() throws EntityNotFoundException {
        Long matchId = 1L;

        when(matchRepository.findById(matchId)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> matchService.getMatchById(matchId)).isInstanceOf(EntityNotFoundException.class);
    }
}