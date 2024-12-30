package com.project.superleague.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.superleague.dto.PlayerInsertDTO;
import com.project.superleague.dto.PlayerReadOnlyDTO;
import com.project.superleague.dto.PlayerUpdateDTO;
import com.project.superleague.model.Player;
import com.project.superleague.model.Team;
import com.project.superleague.service.IPlayerService;
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
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(controllers = PlayerRestController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class PlayerRestTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IPlayerService playerService;

    @Autowired
    private ObjectMapper objectMapper;

    private Player player;
    private Player updatedPlayer;
    private Team team;
    private PlayerInsertDTO playerInsertDTO;
    private PlayerInsertDTO playerInsertDTOInvalid;
    private PlayerUpdateDTO playerUpdateDTO;
    private PlayerUpdateDTO playerUpdateDTOInvalid;

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

        playerInsertDTOInvalid = PlayerInsertDTO.builder()
                .firstname("N")
                .lastname("Papadimitriou")
                .dateOfBirth(LocalDate.parse("2000-02-23"))
                .nationality("Greek")
                .monetaryValue(50000)
                .playerRole("Goalkeeper")
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

        playerUpdateDTOInvalid = PlayerUpdateDTO.builder()
                .id(1L)
                .firstname("N")
                .lastname("Papadimitriou")
                .dateOfBirth(LocalDate.parse("2000-02-23"))
                .nationality("Greek")
                .monetaryValue(50000)
                .playerRole("Goalkeeper")
                .build();
    }

    @Test
    public void PlayerRest_GetPlayersByLastname_ReturnsOk() throws Exception {
        String searchParameter = "Papa";
        List<Player> players = new ArrayList<>();
        players.add(player);

        when(playerService.getPlayerByLastname(searchParameter)).thenReturn(players);

        ResultActions response = mockMvc.perform(get("/api/players?lastname=Papa")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", CoreMatchers.is(players.size())));
    }

    @Test
    public void PlayerRest_GetPlayersByLastname_ReturnsBadRequest() throws Exception {
        String searchParameter = "Xatzi";

        when(playerService.getPlayerByLastname(searchParameter)).thenThrow(EntityNotFoundException.class);

        ResultActions response = mockMvc.perform(get("/api/players?lastname=Xatzi")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void PlayerRest_GetPlayerById_ReturnsOk() throws Exception {
        Long playerId = 1L;

        when(playerService.getPlayerById(playerId)).thenReturn(player);

        ResultActions response = mockMvc.perform(get("/api/players/1")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstname", CoreMatchers.is(player.getFirstname())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastname", CoreMatchers.is(player.getLastname())));
    }

    @Test
    public void PlayerRest_GetPlayerById_ReturnsNotFound() throws Exception {
        Long playerId = 2L;

        when(playerService.getPlayerById(playerId)).thenThrow(EntityNotFoundException.class);

        ResultActions response = mockMvc.perform(get("/api/players/2")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void PlayerRest_AddPlayer_ReturnsCreated() throws Exception {
        when(playerService.insertPlayer(Mockito.any(PlayerInsertDTO.class))).thenReturn(player);

        ResultActions response = mockMvc.perform(post("/api/players")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(playerInsertDTO)));

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().string("Location", "http://localhost/api/players/1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastname", CoreMatchers.is(playerInsertDTO.getLastname())));
    }

    @Test
    public void PlayerRest_AddPlayer_TeamNotFound_ReturnsNotFound() throws Exception {
        when(playerService.insertPlayer(Mockito.any(PlayerInsertDTO.class))).thenThrow(EntityNotFoundException.class);

        ResultActions response = mockMvc.perform(post("/api/players")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(playerInsertDTO)));

        response.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void PlayerRest_AddPlayer_ReturnsServiceUnavailable() throws Exception {
        when(playerService.insertPlayer(Mockito.any(PlayerInsertDTO.class))).thenThrow(Exception.class);

        ResultActions response = mockMvc.perform(post("/api/players")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(playerInsertDTO)));

        response.andExpect(MockMvcResultMatchers.status().isServiceUnavailable());
    }

    @Test
    public void PlayerRest_AddPlayer_ValidationError_ReturnsBadRequest() throws Exception {
        ResultActions response = mockMvc.perform(post("/api/players")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(playerInsertDTOInvalid)));

        response.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void PlayerRest_UpdatePlayer_ReturnsOk() throws Exception {
        when(playerService.updatePlayer(Mockito.any(PlayerUpdateDTO.class))).thenReturn(updatedPlayer);

        ResultActions response = mockMvc.perform(put("/api/players/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(playerUpdateDTO)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is((int) (long) playerUpdateDTO.getId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastname", CoreMatchers.is(playerUpdateDTO.getLastname())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.monetaryValue", CoreMatchers.is(playerUpdateDTO.getMonetaryValue())));
    }

    @Test
    public void PlayerRest_UpdatePlayer_ReturnsUnauthorized() throws Exception {
        ResultActions response = mockMvc.perform(put("/api/players/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(playerUpdateDTO)));

        response.andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    public void PlayerRest_UpdatePlayer_EntityNotFound_ReturnsNotFound() throws Exception {
        when(playerService.updatePlayer(Mockito.any(PlayerUpdateDTO.class))).thenThrow(EntityNotFoundException.class);

        ResultActions response = mockMvc.perform(put("/api/players/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(playerUpdateDTO)));

        response.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void PlayerRest_UpdatePlayer_ValidationError_ReturnsBadRequest() throws Exception {
        ResultActions response = mockMvc.perform(put("/api/players/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(playerUpdateDTOInvalid)));

        response.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void PlayerRest_DeletePlayer_ReturnsOk() throws Exception {
        Long playerId = 1L;

        when(playerService.deletePlayer(playerId)).thenReturn(player);

        ResultActions response = mockMvc.perform(delete("/api/players/1")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is((int) (long) playerId)));
    }

    @Test
    public void PlayerRest_DeletePlayer_ReturnsNotFound() throws Exception {
        Long playerId = 2L;

        when(playerService.deletePlayer(playerId)).thenThrow(EntityNotFoundException.class);

        ResultActions response = mockMvc.perform(delete("/api/players/2")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}