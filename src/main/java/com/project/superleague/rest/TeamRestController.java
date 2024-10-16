package com.project.superleague.rest;

import com.project.superleague.dto.*;
import com.project.superleague.mapper.Mapper;
import com.project.superleague.model.Player;
import com.project.superleague.model.Team;
import com.project.superleague.service.ITeamService;
import com.project.superleague.service.exception.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TeamRestController {
    private final ITeamService teamService;

    @GetMapping("/teams")
    public ResponseEntity<Object> getTeamsByTeamName(@RequestParam("teamname") String teamname) {
        List<Team> teams;

        try {
            teams = teamService.getTeamByName(teamname);
            List<TeamReadOnlyDTO> teamsReadOnlyDTOS = new ArrayList<>();
            for (Team team : teams) {
                teamsReadOnlyDTOS.add(Mapper.mapTeamToReadOnlyDTO(team));
            }
            return new ResponseEntity<>(teamsReadOnlyDTOS, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>("Not found.", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/teams/{id}")
    public ResponseEntity<Object> getTeamById(@PathVariable("id") Long id) {
        Team team;

        try {
            team = teamService.getTeamById(id);
            TeamReadOnlyDTO dto = Mapper.mapTeamToReadOnlyDTO(team);
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>("Not found.", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/teams/all")
    public ResponseEntity<List<TeamReadOnlyDTO>> getAllTeams() {
        List<Team> teams;

        teams = teamService.getAllTeams();
        List<TeamReadOnlyDTO> teamsReadOnlyDTOS = new ArrayList<>();
        for (Team team : teams) {
            teamsReadOnlyDTOS.add(Mapper.mapTeamToReadOnlyDTO(team));
        }
        return new ResponseEntity<>(teamsReadOnlyDTOS, HttpStatus.OK);
    }

    @PostMapping("/teams")
    public ResponseEntity<Object> addTeam(@Valid @RequestBody TeamInsertDTO dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }

        try {
            Team team = teamService.insertTeam(dto);
            TeamReadOnlyDTO teamReadOnlyDTO = Mapper.mapTeamToReadOnlyDTO(team);
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(teamReadOnlyDTO.getId())
                    .toUri();
            return ResponseEntity.created(location).body(teamReadOnlyDTO);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @PutMapping("/teams/{id}")
    public ResponseEntity<Object> updateTeam(@PathVariable("id") Long id, @Valid @RequestBody TeamUpdateDTO dto, BindingResult bindingResult) {
        if (!Objects.equals(id, dto.getId())) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }

        try {
            Team team = teamService.updateTeam(dto);
            TeamReadOnlyDTO teamReadOnlyDTO = Mapper.mapTeamToReadOnlyDTO(team);
            return new ResponseEntity<>(teamReadOnlyDTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>("Not found.", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/teams/{id}")
    public ResponseEntity<Object> deleteTeam(@PathVariable("id") Long id) {
        try {
            Team team = teamService.deleteTeam(id);
            TeamReadOnlyDTO teamReadOnlyDTO = Mapper.mapTeamToReadOnlyDTO(team);
            return new ResponseEntity<>(teamReadOnlyDTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>("Not found.", HttpStatus.BAD_REQUEST);
        }
    }
}