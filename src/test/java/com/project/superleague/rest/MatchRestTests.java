package com.project.superleague.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.superleague.dto.MatchInsertDTO;
import com.project.superleague.dto.MatchUpdateDTO;
import com.project.superleague.model.Match;
import com.project.superleague.model.Player;
import com.project.superleague.model.Team;
import com.project.superleague.service.IMatchService;
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

import java.time.LocalDate;
import java.util.*;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(controllers = MatchRestController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class MatchRestTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IMatchService matchService;

    @Autowired
    private ObjectMapper objectMapper;

    private Match match;
    private Match updatedMatch;
    private Team team1;
    private Team team2;
    private MatchInsertDTO matchInsertDTO;
    private MatchInsertDTO matchInsertDTOInvalid;
    private MatchUpdateDTO matchUpdateDTO;
    private MatchUpdateDTO matchUpdateDTOInvalid;

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

        matchInsertDTO = MatchInsertDTO.builder()
                .matchDate(LocalDate.parse("2024-10-04"))
                .goalsHost(1)
                .goalsGuest(2)
                .hostTeamId(1L)
                .guestTeamId(2L)
                .build();

        matchUpdateDTO = MatchUpdateDTO.builder()
                .id(1L)
                .matchDate(LocalDate.parse("2024-11-04"))
                .goalsHost(1)
                .goalsGuest(0)
                .hostTeamId(1L)
                .guestTeamId(2L)
                .build();

        matchInsertDTOInvalid = MatchInsertDTO.builder()
                .matchDate(LocalDate.parse("2024-10-04"))
                .goalsHost(null)
                .goalsGuest(2)
                .hostTeamId(1L)
                .guestTeamId(2L)
                .build();

        matchUpdateDTOInvalid = MatchUpdateDTO.builder()
                .id(1L)
                .matchDate(LocalDate.parse("2024-11-04"))
                .goalsHost(1)
                .goalsGuest(null)
                .hostTeamId(1L)
                .guestTeamId(2L)
                .build();

        updatedMatch = Match.builder()
                .id(1L)
                .matchDate(LocalDate.parse("2024-11-04"))
                .goalsHost(1)
                .goalsGuest(0)
                .build();

        updatedMatch.addHostTeam(team1);
        updatedMatch.addGuestTeam(team2);
    }

    @Test
    public void MatchRest_GetMatchesByDate_ReturnsOk() throws Exception {
        LocalDate searchParameter = LocalDate.parse("2024-10-04");
        List<Match> matches = new ArrayList<>();
        matches.add(match);

        when(matchService.getMatchByDate(searchParameter)).thenReturn(matches);

        ResultActions response = mockMvc.perform(get("/api/matches?date=04102024")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", CoreMatchers.is(matches.size())));
    }

    @Test
    public void MatchRest_GetMatchesByDate_ReturnsBadRequest() throws Exception {
        LocalDate searchParameter = LocalDate.parse("2024-10-08");

        when(matchService.getMatchByDate(searchParameter)).thenThrow(EntityNotFoundException.class);

        ResultActions response = mockMvc.perform(get("/api/matches?date=08102024")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void MatchRest_GetMatchById_ReturnsOk() throws Exception {
        Long matchId = 1L;

        when(matchService.getMatchById(matchId)).thenReturn(match);

        ResultActions response = mockMvc.perform(get("/api/matches/1")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.matchDate", CoreMatchers.is("2024-10-04")));
    }

    @Test
    public void MatchRest_GetMatchById_ReturnsBadRequest() throws Exception {
        Long matchId = 2L;

        when(matchService.getMatchById(matchId)).thenThrow(EntityNotFoundException.class);

        ResultActions response = mockMvc.perform(get("/api/matches/2")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}