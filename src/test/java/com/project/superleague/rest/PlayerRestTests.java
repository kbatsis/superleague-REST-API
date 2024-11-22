package com.project.superleague.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.superleague.dto.PlayerInsertDTO;
import com.project.superleague.dto.PlayerReadOnlyDTO;
import com.project.superleague.dto.PlayerUpdateDTO;
import com.project.superleague.model.Player;
import com.project.superleague.service.IPlayerService;
import com.project.superleague.service.exception.EntityNotFoundException;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

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
    private PlayerInsertDTO playerInsertDTO;
    private PlayerUpdateDTO playerUpdateDTO;
    private PlayerReadOnlyDTO playerReadOnlyDTO;

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

        playerReadOnlyDTO = PlayerReadOnlyDTO.builder()
                .id(1L)
                .firstname("Nikos")
                .lastname("Papadimitriou")
                .dateOfBirth(new GregorianCalendar(2000, Calendar.FEBRUARY, 23).getTime())
                .nationality("Greek")
                .monetaryValue(60000)
                .playerRole("Goalkeeper")
                .teamId(1L)
                .build();
    }

    @Test
    public void PlayerRest_GetPlayersByLastname_ReturnsResponse() throws Exception {
        String searchParameter = "Papa";
        List<Player> players = new ArrayList<>();
        players.add(player);
        List<PlayerReadOnlyDTO> playerReadOnlyDTOS = new ArrayList<>();
        playerReadOnlyDTOS.add(playerReadOnlyDTO);

        when(playerService.getPlayerByLastname(searchParameter)).thenReturn(players);

        ResultActions response = mockMvc.perform(get("/api/players?lastname=Papa")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(playerReadOnlyDTOS)));

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
    public void PlayerRest_GetPlayerById_ReturnsResponse() throws Exception {
        Long playerId = 1L;

        when(playerService.getPlayerById(playerId)).thenReturn(player);

        ResultActions response = mockMvc.perform(get("/api/players/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(playerReadOnlyDTO)));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstname", CoreMatchers.is(player.getFirstname())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastname", CoreMatchers.is(player.getLastname())));
    }

    @Test
    public void PlayerRest_GetPlayerById_ReturnsBadRequest() throws Exception {
        Long playerId = 2L;

        when(playerService.getPlayerById(playerId)).thenThrow(EntityNotFoundException.class);

        ResultActions response = mockMvc.perform(get("/api/players/2")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
