package com.project.superleague.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.superleague.dto.PlayerInsertDTO;
import com.project.superleague.dto.PlayerUpdateDTO;
import com.project.superleague.dto.TeamInsertDTO;
import com.project.superleague.dto.TeamUpdateDTO;
import com.project.superleague.model.Player;
import com.project.superleague.model.Team;
import com.project.superleague.service.ITeamService;
import com.project.superleague.service.exception.EntityNotFoundException;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@WebMvcTest(controllers = TeamRestController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class TeamRestTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ITeamService teamService;

    @Autowired
    private ObjectMapper objectMapper;

    private Team team;
    private Team updatedTeam;
    private TeamInsertDTO teamInsertDTO;
    private TeamInsertDTO teamInsertDTOInvalid;
    private TeamUpdateDTO teamUpdateDTO;
    private TeamUpdateDTO teamUpdateDTOInvalid;

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

        teamInsertDTOInvalid = TeamInsertDTO.builder()
                .teamName("A")
                .foundationYear(1914)
                .cityName("Thessaloniki")
                .stadiumName("Kleanthis Vikelidis")
                .coachFirstname("Akis")
                .coachLastname("Mantzios")
                .presidentFirstname("Eirini")
                .presidentLastname("Karypidou")
                .build();

        teamUpdateDTOInvalid = TeamUpdateDTO.builder()
                .id(1L)
                .teamName("A")
                .foundationYear(1914)
                .cityName("Thessaloniki")
                .stadiumName("Kleanthis Vikelidis")
                .coachFirstname("Akis")
                .coachLastname("Mantzios")
                .presidentFirstname("Eirini")
                .presidentLastname("Karypidou")
                .build();
    }

    @Test
    public void TeamRest_GetTeamsByTeamName_ReturnsOk() throws Exception {
        String searchParameter = "Ar";
        List<Team> teams = new ArrayList<>();
        teams.add(team);

        when(teamService.getTeamByName(searchParameter)).thenReturn(teams);

        ResultActions response = mockMvc.perform(get("/api/teams?teamname=Ar")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", CoreMatchers.is(teams.size())));
    }

    @Test
    public void TeamRest_GetTeamsByTeamName_ReturnsBadRequest() throws Exception {
        String searchParameter = "Ar";

        when(teamService.getTeamByName(searchParameter)).thenThrow(EntityNotFoundException.class);

        ResultActions response = mockMvc.perform(get("/api/teams?lastname=Oly")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void TeamRest_GetTeamById_ReturnsOk() throws Exception {
        Long teamId = 1L;

        when(teamService.getTeamById(teamId)).thenReturn(team);

        ResultActions response = mockMvc.perform(get("/api/teams/1")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.teamName", CoreMatchers.is(team.getTeamName())));
    }

    @Test
    public void TeamRest_GetTeamById_ReturnsNotFound() throws Exception {
        Long teamId = 2L;

        when(teamService.getTeamById(teamId)).thenThrow(EntityNotFoundException.class);

        ResultActions response = mockMvc.perform(get("/api/teams/2")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void TeamRest_AddTeam_ReturnsCreated() throws Exception {
        when(teamService.insertTeam(Mockito.any(TeamInsertDTO.class))).thenReturn(team);

        ResultActions response = mockMvc.perform(post("/api/teams")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(teamInsertDTO)));

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().string("Location", "http://localhost/api/teams/1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.teamName", CoreMatchers.is(teamInsertDTO.getTeamName())));
    }

    @Test
    public void TeamRest_AddTeam_ReturnsServiceUnavailable() throws Exception {
        when(teamService.insertTeam(Mockito.any(TeamInsertDTO.class))).thenThrow(Exception.class);

        ResultActions response = mockMvc.perform(post("/api/teams")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(teamInsertDTO)));

        response.andExpect(MockMvcResultMatchers.status().isServiceUnavailable());
    }

    @Test
    public void TeamRest_AddTeam_ValidationError_ReturnsBadRequest() throws Exception {
        ResultActions response = mockMvc.perform(post("/api/teams")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(teamInsertDTOInvalid)));

        response.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void TeamRest_UpdateTeam_ReturnsOk() throws Exception {
        when(teamService.updateTeam(Mockito.any(TeamUpdateDTO.class))).thenReturn(updatedTeam);

        ResultActions response = mockMvc.perform(put("/api/teams/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(teamUpdateDTO)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is((int) (long) teamUpdateDTO.getId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.teamName", CoreMatchers.is(teamUpdateDTO.getTeamName())));
    }

    @Test
    public void TeamRest_UpdateTeam_ReturnsUnauthorized() throws Exception {
        ResultActions response = mockMvc.perform(put("/api/teams/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(teamUpdateDTO)));

        response.andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    public void TeamRest_UpdateTeam_TeamNotFound_ReturnsNotFound() throws Exception {
        when(teamService.updateTeam(Mockito.any(TeamUpdateDTO.class))).thenThrow(EntityNotFoundException.class);

        ResultActions response = mockMvc.perform(put("/api/teams/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(teamUpdateDTO)));

        response.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void TeamRest_UpdateTeam_ValidationError_ReturnsBadRequest() throws Exception {
        ResultActions response = mockMvc.perform(put("/api/teams/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(teamUpdateDTOInvalid)));

        response.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void TeamRest_DeleteTeam_ReturnsOk() throws Exception {
        Long teamId = 1L;

        when(teamService.deleteTeam(teamId)).thenReturn(team);

        ResultActions response = mockMvc.perform(delete("/api/teams/1")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is((int) (long) teamId)));
    }

    @Test
    public void TeamRest_DeleteTeam_ReturnsNotFound() throws Exception {
        Long teamId = 2L;

        when(teamService.deleteTeam(teamId)).thenThrow(EntityNotFoundException.class);

        ResultActions response = mockMvc.perform(delete("/api/teams/2")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}