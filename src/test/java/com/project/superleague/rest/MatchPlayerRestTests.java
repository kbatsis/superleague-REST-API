package com.project.superleague.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.superleague.dto.MatchPlayerInsertDTO;
import com.project.superleague.dto.MatchPlayerUpdateDTO;
import com.project.superleague.dto.PlayerInsertDTO;
import com.project.superleague.dto.PlayerUpdateDTO;
import com.project.superleague.model.Match;
import com.project.superleague.model.MatchPlayer;
import com.project.superleague.model.Player;
import com.project.superleague.model.Team;
import com.project.superleague.service.IMatchPlayerService;
import com.project.superleague.service.exception.EntityAlreadyExistsException;
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

import java.time.LocalDate;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@WebMvcTest(controllers = MatchPlayerRestController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class MatchPlayerRestTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    IMatchPlayerService matchPlayerService;

    @Autowired
    private ObjectMapper objectMapper;

    private MatchPlayer matchPlayer;
    private Match match;
    private Player player;
    private Team team1;
    private Team team2;
    private MatchPlayer updatedMatchPlayer;
    private MatchPlayerInsertDTO matchPlayerInsertDTO;
    private MatchPlayerUpdateDTO matchPlayerUpdateDTO;

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
    }

    @Test
    public void MatchPlayerRest_GetMatchPlayerByMatchIdAndPlayerId_ReturnsOk() throws Exception {
        Long matchId = 1L;
        Long playerId = 1L;

        when(matchPlayerService.getMatchPlayerByMatchIdAndPlayerId(matchId, playerId)).thenReturn(matchPlayer);

        ResultActions response = mockMvc.perform(get("/api/matchesplayers/1/1")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.matchId", CoreMatchers.is((int) (long) matchId)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.playerId", CoreMatchers.is((int) (long) playerId)));
    }

    @Test
    public void MatchPlayerRest_GetMatchPlayerByMatchIdAndPlayerId_ReturnsNotFound() throws Exception {
        Long matchId = 1L;
        Long playerId = 2L;

        when(matchPlayerService.getMatchPlayerByMatchIdAndPlayerId(matchId, playerId)).thenThrow(EntityNotFoundException.class);

        ResultActions response = mockMvc.perform(get("/api/matchesplayers/1/2")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void MatchPlayerRest_AddMatchPlayer_ReturnsCreated() throws Exception {
        when(matchPlayerService.insertMatchPlayer(Mockito.any(MatchPlayerInsertDTO.class))).thenReturn(matchPlayer);

        ResultActions response = mockMvc.perform(post("/api/matchesplayers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(matchPlayerInsertDTO)));

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().string("Location", "http://localhost/api/matchesplayers/1/1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.matchId", CoreMatchers.is((int) (long) matchPlayerInsertDTO.getMatchId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.playerId", CoreMatchers.is((int) (long) matchPlayerInsertDTO.getPlayerId())));
    }

    @Test
    public void MatchPlayerRest_AddMatchPlayer_EntityNotFound_ReturnsNotFound() throws Exception {
        when(matchPlayerService.insertMatchPlayer(Mockito.any(MatchPlayerInsertDTO.class))).thenThrow(EntityNotFoundException.class);

        ResultActions response = mockMvc.perform(post("/api/matchesplayers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(matchPlayerInsertDTO)));

        response.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void MatchPlayerRest_AddMatchPlayer_AlreadyExists_ReturnsBadRequest() throws Exception {
        when(matchPlayerService.insertMatchPlayer(Mockito.any(MatchPlayerInsertDTO.class))).thenThrow(EntityAlreadyExistsException.class);

        ResultActions response = mockMvc.perform(post("/api/matchesplayers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(matchPlayerInsertDTO)));

        response.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void MatchPlayerRest_AddMatchPlayer_ReturnsServiceUnavailable() throws Exception {
        when(matchPlayerService.insertMatchPlayer(Mockito.any(MatchPlayerInsertDTO.class))).thenThrow(Exception.class);

        ResultActions response = mockMvc.perform(post("/api/matchesplayers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(matchPlayerInsertDTO)));

        response.andExpect(MockMvcResultMatchers.status().isServiceUnavailable());
    }

    @Test
    public void MatchPlayerRest_UpdateMatchPlayer_ReturnsOk() throws Exception {
        when(matchPlayerService.updateMatchPlayer(Mockito.any(MatchPlayerUpdateDTO.class))).thenReturn(updatedMatchPlayer);

        ResultActions response = mockMvc.perform(put("/api/matchesplayers/1/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(matchPlayerUpdateDTO)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is((int) (long) matchPlayerUpdateDTO.getId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.matchId", CoreMatchers.is((int) (long) matchPlayerUpdateDTO.getMatchId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.playerId", CoreMatchers.is((int) (long) matchPlayerUpdateDTO.getPlayerId())));
    }

    @Test
    public void MatchPlayerRest_UpdateMatchPlayer_ReturnsUnauthorized() throws Exception {
        ResultActions response = mockMvc.perform(put("/api/matchesplayers/1/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(matchPlayerUpdateDTO)));

        response.andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    public void MatchPlayerRest_UpdateMatchPlayer_EntityNotFound_ReturnsNotFound() throws Exception {
        when(matchPlayerService.updateMatchPlayer(Mockito.any(MatchPlayerUpdateDTO.class))).thenThrow(EntityNotFoundException.class);

        ResultActions response = mockMvc.perform(put("/api/matchesplayers/1/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(matchPlayerUpdateDTO)));

        response.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void MatchPlayerRest_DeleteMatchPlayer_ReturnsOk() throws Exception {
        Long matchId = 1L;
        Long playerId = 1L;

        when(matchPlayerService.deleteMatchPlayer(matchId, playerId)).thenReturn(matchPlayer);

        ResultActions response = mockMvc.perform(delete("/api/matchesplayers/1/1")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is(1)));
    }

    @Test
    public void MatchPlayerRest_DeleteMatchPlayer_ReturnsNotFound() throws Exception {
        Long matchId = 1L;
        Long playerId = 2L;

        when(matchPlayerService.deleteMatchPlayer(matchId, playerId)).thenThrow(EntityNotFoundException.class);

        ResultActions response = mockMvc.perform(delete("/api/matchesplayers/1/2")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}