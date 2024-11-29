package com.project.superleague.service;

import com.project.superleague.dto.PlayerInsertDTO;
import com.project.superleague.dto.PlayerUpdateDTO;
import com.project.superleague.dto.TeamInsertDTO;
import com.project.superleague.dto.TeamUpdateDTO;
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

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TeamServiceTests {
    @Mock
    private TeamRepository teamRepository;

    @Mock
    private PlayerRepository playerRepository;

    @InjectMocks
    private TeamServiceImpl teamService;

    private Team team;
    private TeamInsertDTO teamInsertDTO;
    private TeamUpdateDTO teamUpdateDTO;
    private Player player;
    private Team teamNull;
    private Team updatedTeam;

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
                .dateOfBirth(new GregorianCalendar(2000, Calendar.FEBRUARY, 23).getTime())
                .nationality("Greek")
                .monetaryValue(50000)
                .playerRole("Goalkeeper")
                .build();

        player.addTeam(team);

        updatedTeam = Team.builder()
                .id(1L)
                .teamName("Arhs")
                .foundationYear(1914)
                .cityName("Thessaloniki")
                .stadiumName("Kleanthis Vikelidis")
                .coachFirstname("Akis")
                .coachLastname("Mantzios")
                .presidentFirstname("Eirini")
                .presidentLastname("Karypidou")
                .build();

        teamInsertDTO = TeamInsertDTO.builder()
                .teamName("Aris")
                .foundationYear(1914)
                .cityName("Thessaloniki")
                .stadiumName("Kleanthis Vikelidis")
                .coachFirstname("Akis")
                .coachLastname("Mantzios")
                .presidentFirstname("Eirini")
                .presidentLastname("Karypidou")
                .build();

        teamUpdateDTO = TeamUpdateDTO.builder()
                .id(1L)
                .teamName("Arhs")
                .foundationYear(1914)
                .cityName("Thessaloniki")
                .stadiumName("Kleanthis Vikelidis")
                .coachFirstname("Akis")
                .coachLastname("Mantzios")
                .presidentFirstname("Eirini")
                .presidentLastname("Karypidou")
                .build();

        teamNull = Team.builder().build();
    }

    @Test
    public void TeamService_InsertTeam_ReturnsTeamDTOIdNotNull() throws Exception {
        when(teamRepository.save(Mockito.any(Team.class))).thenReturn(team);

        Team savedTeam = teamService.insertTeam(teamInsertDTO);

        Assertions.assertThat(savedTeam.getId()).isNotNull();
    }

    @Test
    public void TeamService_InsertTeam_ThrowsException() throws Exception {
        when(teamRepository.save(Mockito.any(Team.class))).thenReturn(teamNull);

        Assertions.assertThatThrownBy(() -> teamService.insertTeam(teamInsertDTO)).isInstanceOf(Exception.class);
    }

    @Test
    public void TeamService_UpdateTeam_ReturnsUpdatedTeamDTO() throws EntityNotFoundException {
        when(teamRepository.findById(teamUpdateDTO.getId())).thenReturn(Optional.ofNullable(team));
        when(teamRepository.save(Mockito.any(Team.class))).thenReturn(updatedTeam);

        Team updateReturn = teamService.updateTeam(teamUpdateDTO);

        Assertions.assertThat(updateReturn.getTeamName()).isEqualTo("Arhs");
    }

    @Test
    public void TeamService_UpdateTeam_ThrowsEntityNotFoundException() throws EntityNotFoundException {
        when(teamRepository.findById(teamUpdateDTO.getId())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> teamService.updateTeam(teamUpdateDTO)).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    public void TeamService_DeleteTeam_ReturnsVoid() {
        Long teamId = 1L;

        when(teamRepository.findById(teamId)).thenReturn(Optional.ofNullable(team));

        assertAll(() -> teamService.deleteTeam(teamId));
        Assertions.assertThat(player.getTeam()).isNull();
    }

    @Test
    public void TeamService_DeleteTeam_ThrowsEntityNotFoundException() {
        Long teamId = 1L;

        when(teamRepository.findById(teamId)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> teamService.deleteTeam(teamId)).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    public void TeamService_GetTeamByName_ReturnsTeams() throws EntityNotFoundException {
        String searchParameter = "Ar";
        List<Team> teams = new ArrayList<>();
        teams.add(team);

        when(teamRepository.findByTeamNameStartingWith(searchParameter)).thenReturn(teams);

        List<Team> teamsReturn = teamService.getTeamByName(searchParameter);

        Assertions.assertThat(teamsReturn.size()).isEqualTo(1);
    }

    @Test
    public void TeamService_GetTeamByName_ThrowsEntityNotFoundException() throws EntityNotFoundException {
        String searchParameter = "Ar";
        List<Team> teams = new ArrayList<>();

        when(teamRepository.findByTeamNameStartingWith(searchParameter)).thenReturn(teams);

        Assertions.assertThatThrownBy(() -> teamService.getTeamByName(searchParameter)).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    public void TeamService_GetTeamById_ReturnsTeam() throws EntityNotFoundException {
        Long teamId = 1L;

        when(teamRepository.findById(teamId)).thenReturn(Optional.ofNullable(team));

        Team teamReturn = teamService.getTeamById(teamId);

        Assertions.assertThat(teamReturn).isNotNull();
    }

    @Test
    public void TeamService_GetTeamById_ThrowsEntityNotFoundException() throws EntityNotFoundException {
        Long teamId = 1L;

        when(teamRepository.findById(teamId)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> teamService.getTeamById(teamId)).isInstanceOf(EntityNotFoundException.class);
    }
}